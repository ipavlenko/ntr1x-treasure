package com.ntr1x.treasure.web.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.CopyGroup;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.attributes.AttributeEntity;
import com.ntr1x.treasure.web.model.attributes.AttributeValue;
import com.ntr1x.treasure.web.model.catalog.CompanyEntity;
import com.ntr1x.treasure.web.model.purchase.GoodCategory;
import com.ntr1x.treasure.web.model.purchase.GoodEntity;
import com.ntr1x.treasure.web.model.purchase.PurchaseEntity;
import com.ntr1x.treasure.web.model.purchase.ResourceType;
import com.ntr1x.treasure.web.model.security.SecuritySession;
import com.ntr1x.treasure.web.repository.GoodCategoryRepository;
import com.ntr1x.treasure.web.repository.GoodEntityRepository;
import com.ntr1x.treasure.web.utils.map.FilterValue;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api("Goods")
@Component
@Path("/ws/catalog/goods")
@Slf4j
public class GoodResource {

	@Inject
	private SecuritySession session;

	@Inject
    private GoodCategoryRepository categories;
	
	@Inject
    private GoodEntityRepository goods;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddObj {
		private long pid;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddMod {
		private long goodId;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class FilterGoods {
		@XmlElement
		private List<FilterValue> settedAttributes;
		private long pid;

		private Map<String, List<String>> getArrValuesMap(){

			Map<String, List<String>> attributes = new HashMap<>();
			if (settedAttributes != null){
				for (FilterValue val : settedAttributes){
					if (attributes.get(val.getName()) != null){
						attributes.get(val.getName()).add(val.getValue());
					} else {
						attributes.put(val.getName(), new ArrayList<>());
						attributes.get(val.getName()).add(val.getValue());
					}
				}
			}
			return attributes;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class AtrGoodList implements Serializable {
		private static final long serialVersionUID = -6410688391927286753L;
		private String attrName;
		private String value;
		private List<GoodEntity> goods;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class CatValueList implements Serializable{
		private static final long serialVersionUID = -14038014837124724L;
		private GoodCategory category;
		private List<GoodCategory> childs;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Filtered {
		private List<CatValueList> allCategories;
		private List<AtrGoodList> goods;
	}

	@PersistenceContext
	private EntityManager em;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public GoodEntity good(
			@PathParam("id") long id
	) {
		try {
			GoodEntity good = em.find(GoodEntity.class, id);

			return good;
		} catch (Exception e){
			throw  e;
		}
	}

	@GET
	@Path("/root/attribute/values")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional()
	//	@RolesAllowed("admin")
	public List<AttributeValue> attributeValues() {
		try {

			List<AttributeValue> result = em.createNamedQuery("AttributeValue.accessibleOfResType", AttributeValue.class)
					.setParameter("resType", ResourceType.ROOT_GOOD)
					.getResultList();

			return result;
		} catch (Exception e){
			throw e;
		}
	}

	@POST
	@Path("/root/attribute/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("admin")						//TODO
	public Response updateAttribute(AttributeValue attribute) {

		try {
			if (attribute.getAction() != null){
				switch(attribute.getAction()) {
					case REMOVE: {
						em.remove(em.find(AttributeEntity.class, attribute.getAttribute().getId()));
						break;
					}
					case ADD: {
						if (em.find(AttributeValue.class, attribute.getId()) != null || em.find(AttributeEntity.class, attribute.getAttribute().getId()) != null)
							return Response.status(Response.Status.BAD_REQUEST).build();

						em.persist(attribute.getAttribute());
						em.flush();

						List<GoodEntity> goods = this.goods.findAll();

						for (GoodEntity good : goods){

							AttributeValue val = new AttributeValue();
							val.setValue(attribute.getValue());
							val.setRelate(good);
							val.setAttribute(attribute.getAttribute());
							good.getAttributes().add(val);
							em.merge(good);
						}
						break;
					}
					case UPDATE: {

						GoodEntity baseGood = this.goods.findByResType(ResourceType.ROOT_GOOD);
						AttributeValue val = baseGood.getAttributes().stream().filter(p -> p.getId() == attribute.getId()).findFirst().get();

						val.getAttribute().setName(attribute.getAttribute().getName());
						val.getAttribute().setTitle(attribute.getAttribute().getTitle());
						val.getAttribute().setOptions(attribute.getAttribute().getOptions());
						val.getAttribute().setOrder(attribute.getAttribute().getOrder());
						val.setValue(attribute.getValue());

						em.merge(val);

						break;
					}
				}
				em.flush();

				return Response.ok().build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e){
			throw e;
		}
	}

    /**
     * Создает товар внутри указанной поставки, с предустановленными опциями по умолчанию
     * @param addObj параметры добавления
     * @return
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
	//	@RolesAllowed("user")
    public Response addGood(AddObj addObj) {

		if (session != null){
			PurchaseEntity purchase = em.find(PurchaseEntity.class, addObj.getPid());

			if (purchase.getUser().getId() == session.getUser().getId()){
				if (purchase.getStatus() == PurchaseEntity.Status.NEW
						|| purchase.getStatus() == PurchaseEntity.Status.MODERATION
						|| purchase.getStatus() == PurchaseEntity.Status.OPEN){

				    GoodEntity baseGood = this.goods.findByResType(ResourceType.ROOT_GOOD);
				    
					CopyGroup group = new CopyGroup();
					group.setShouldResetPrimaryKey(true);
					GoodEntity newGood = (GoodEntity) em.unwrap(JpaEntityManager.class).copy(baseGood, group);

					newGood.setResType(ResourceType.EXTENDED);

					newGood.setTitle("Новый товар");
					newGood.setStatus(GoodEntity.Status.ACTIVE);

					List<GoodCategory> categories = this.categories.findAll();

					newGood.getCategories().add(categories.get(0));                 //TODO это дефолтный вариант выбора категории, в  реальности его не будет

					newGood.setAlias(String.format("/purchase/%s/good/", purchase.getId()));
					newGood.setPurchase(purchase);

					em.persist(newGood);
					em.flush();

					newGood.setAlias(String.format("/purchase/%s/good/%s", purchase.getId(), newGood.getId()));
					em.persist(newGood);

					em.merge(purchase);
					em.flush();

					return Response.ok(em.find(GoodEntity.class, newGood.getId())).build();
				}
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@POST
	@Path("/mod/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("user")
	public Response addModification(AddMod modObj) {

		if (session != null){
			GoodEntity good = em.find(GoodEntity.class, modObj.goodId);

			if (good.getPurchase().getUser().getId() == session.getUser().getId()){
				CopyGroup group = new CopyGroup();
				group.setShouldResetPrimaryKey(true);
				GoodEntity modGood = (GoodEntity) em.unwrap(JpaEntityManager.class).copy(good, group);

				modGood.setResType(ResourceType.EXTENDED);

				modGood.setAlias(String.format("/purchase/%s/good/", modGood.getPurchase().getId()));
				modGood.getPersonalImages().clear();

				em.persist(modGood);
				em.flush();

				modGood.setAlias(String.format("/purchase/%s/good/%s", modGood.getPurchase().getId(), modGood.getId()));
				em.persist(modGood);

				em.merge(modGood);
				em.flush();

				return Response.ok(em.find(GoodEntity.class, modGood.getId())).build();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response updateGood(GoodEntity good) {

		try {
			if (session != null){
				GoodEntity oldGood = em.find(GoodEntity.class, good.getId());

				for (AttributeValue val : good.getAttributes()){
					oldGood.getAttributes().stream().filter(g -> g.getAttribute().getName().equals(val.getAttribute().getName())).findFirst().get().setValue(val.getValue());
				}

				oldGood.setPrice(good.getPrice());
				oldGood.setQuantity(good.getQuantity());
				oldGood.setImages(good.getImages());
				oldGood.setPersonalImages(good.getPersonalImages());
				oldGood.setTitle(good.getTitle());
				oldGood.setTags(good.getTags());
				oldGood.setSizeRange(good.getSizeRange());

				if (good.getCompany() != null){
					CompanyEntity company = em.find(CompanyEntity.class, good.getCompany().getId());
					oldGood.setCompany(company);
				}

				List<GoodCategory> categories = em.createNamedQuery("GoodCategory.list", GoodCategory.class).getResultList();

				oldGood.getCategories().clear();

				if (good.getCategories() != null){

					Set<GoodCategory> ctgs = new HashSet<>();

					for (GoodCategory cat : good.getCategories()){
						GoodCategory c = categories.stream().filter(cc -> cc.getId() == cat.getId()).findFirst().get();
						ctgs.add(c);
						if (c.getParent() != null)
							ctgs.add(c.getParent());
					}

					oldGood.setCategories(new ArrayList<>(ctgs));
				}

				em.merge(oldGood);
				em.flush();

				return Response.ok(em.find(GoodEntity.class, good.getId())).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (Exception e){
			throw e;
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response delete(
			@PathParam("id") long id
	) {
		try {
			if (session != null){
				GoodEntity good = em.find(GoodEntity.class, id);

				if (good.getPurchase().getUser().getId() == session.getUser().getId()){
					if (good.getPurchase().getStatus() == PurchaseEntity.Status.NEW
							|| good.getPurchase().getStatus() == PurchaseEntity.Status.MODERATION){
						good.setStatus(GoodEntity.Status.DELETED);

						em.merge(good);
						em.flush();

						return Response.ok().build();
					}
					else {
						return Response.status(Response.Status.BAD_REQUEST).build();
					}
				} else {
					return Response.status(Response.Status.UNAUTHORIZED).build();
				}
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (Throwable th){
			throw th;
		}
	}

	@POST
	@Path("/filter/goods")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response doFilter(FilterGoods filter) {

		try {
			if (filter.getPid() > 0){
				Map<String, List<String>> settedAttributes = filter.getArrValuesMap();

				String qStr = "	SELECT g ";

				qStr = String.format(" %s FROM GoodEntity g INNER JOIN g.purchase p ", qStr);
				qStr = String.format(" %s ON p.id = %s ", qStr, filter.getPid());

				for (Map.Entry<String, List<String>> entry : settedAttributes.entrySet()){
					qStr = String.format(" %s INNER JOIN g.attributes cav_%s ", qStr, entry.getKey());
					qStr = String.format(" %s ON ( NULL IS NULL ", qStr, entry.getKey());
					for (String v : entry.getValue()){
						qStr = String.format(" %s AND cav_%s.value = :cav_%s ", qStr, entry.getKey(), entry.getKey());
					}
					qStr = String.format(" %s ) ", qStr);
				}

				qStr = String.format(" %s WHERE g.status != :status ", qStr);

				Query query = em.createQuery(qStr);

				query.setParameter("status", GoodEntity.Status.DELETED);

				for (Map.Entry<String, List<String>> entry : settedAttributes.entrySet()){
					for (String v : entry.getValue()){
						query = query.setParameter(String.format("cav_%s", entry.getKey()), v);
					}
				}

				List<GoodEntity> result = query.getResultList();
				List<AtrGoodList> agl = new ArrayList<>();
				Map<String, AtrGoodList> attributesMap = new HashMap<>();

				for (GoodEntity g : result){
					g.getPurchase();
					AttributeValue atr = g.getAttributes().stream().filter(a -> a.getAttribute().getName().equals("color")).findFirst().get();

					if (attributesMap.get(atr.getValue()) != null){
						attributesMap.get(atr.getValue()).getGoods().add(g);
					}
					else{
						attributesMap.put(atr.getValue(), new AtrGoodList(atr.getAttribute().getName(), atr.getValue(), new ArrayList<>()));
						attributesMap.get(atr.getValue()).getGoods().add(g);
					}

				}

				for (Map.Entry<String, AtrGoodList> entry : attributesMap.entrySet()){
					agl.add(entry.getValue());
				}

				List<CatValueList> cvl = new ArrayList<>();
				Map<GoodCategory, List<GoodCategory>> catMap = new HashMap<>();

				List<GoodCategory> categories = em.createNamedQuery("GoodCategory.list", GoodCategory.class).getResultList();

				for (GoodCategory ctg : categories) {
					if (ctg.getParent() != null && catMap.get(ctg.getParent()) != null) {
						catMap.get(ctg.getParent()).add(ctg);
					} else if (ctg.getParent() != null && catMap.get(ctg.getParent()) == null){
						catMap.put(ctg.getParent(), new ArrayList<>());
						catMap.get(ctg.getParent()).add(ctg);
					} else if (ctg.getParent() == null && catMap.get(ctg) == null ){
						catMap.put(ctg, new ArrayList<>());
					}
				}

				for (Map.Entry<GoodCategory, List<GoodCategory>> entry : catMap.entrySet()){
					cvl.add(new CatValueList(entry.getKey(), entry.getValue()));
				}

				return Response.ok(new Filtered(cvl, agl)).build();
			}

			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e){
			log.error("{}", e);
			throw e;
		}
	}

}
