package com.ntr1x.treasure.web.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.CopyGroup;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.Aspect;
import com.ntr1x.treasure.web.model.Company;
import com.ntr1x.treasure.web.model.Good;
import com.ntr1x.treasure.web.model.Modification;
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.CategoryRepository;
import com.ntr1x.treasure.web.repository.GoodRepository;
import com.ntr1x.treasure.web.resources.PurchasesResource.CatValueList;
import com.ntr1x.treasure.web.resources.PurchasesResource.Filtered;
import com.ntr1x.treasure.web.services.IParamService;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api("Goods")
@Component
@Path("goods")
@Slf4j
public class GoodResource {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private IParamService params;
    
	@Inject
    private CategoryRepository categories;
	
	@Inject
    private GoodRepository goods;

	@GET
	@Path("/i/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Good select(@PathParam("id") long id) {
	    
	    Good good = em.find(Good.class, id);
        return good;
	}
	
	@POST
	@Path("/purchases/i/{id}/goods")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///purchases/i/{id}:admin" })
    public Good create(@PathParam("id") long id, CreateGoodRequest request) {

	    Purchase p = em.find(Purchase.class, id);
	    
	    switch (p.getStatus()) {
    	    case NEW:
    	    case MODERATION:
    	    case OPEN:
    	        break;
    	    default:
    	        throw new WebApplicationException("Current state of the purchase doesn't allow modification", Response.Status.CONFLICT);
	    }
	    
	    Good g = new Good(); {
	        
	        g.setPurchase(p);
	        g.setTitle(request.title);
	        g.setPromo(request.promo);
	        
	        em.persist(g);
	        em.flush();
	        
	        security.register(g, ResourceUtils.alias(null, "goods/i", g));
	        
	        params.createParams(g, request.params);
	    }
	    
	    if (request.modifications != null) {
	        
	        for (CreateGoodRequest.Modification modification : request.modifications) {
	            
	            Modification m = new Modification(); {
	                
	                m.setGood(g);
	                m.setPrice(modification.price);
	                m.setSizeRange(modification.sizeRange);
	                
	                em.persist(m);
	                em.flush();
	                
	                security.register(m, ResourceUtils.alias(g, "modifications/i", g));
	                
	                params.createParams(m, modification.params);
	            }
	        }
	    }
	    
	    return g;
    }
	
	@POST
	@Path("/purchases/i/{id}/goods/i/{good}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///purchases/i/{id}:admin" })
    public Good update(@PathParam("id") long id, @PathParam("good") long good, UpdateGoodRequest request) {

        Purchase p = em.find(Purchase.class, id);
        
        switch (p.getStatus()) {
            case NEW:
            case MODERATION:
            case OPEN:
                break;
            default:
                throw new WebApplicationException("Current state of the purchase doesn't allow modification", Response.Status.CONFLICT);
        }
        
        Good g = em.find(Good.class, good); {
            
            if (g.getPurchase().getId() != p.getId()) {
                throw new WebApplicationException("Good belongs to another Purchase", Response.Status.CONFLICT);
            }
            
            g.setTitle(request.title);
            g.setPromo(request.promo);
            
            em.merge(g);
            em.flush();
            
            params.updateParams(g, request.params);
        }
        
        if (request.modifications != null) {
            
            for (UpdateGoodRequest.Modification modification : request.modifications) {
                
                Modification m = new Modification(); {
                    
                    m.setGood(g);
                    m.setPrice(modification.price);
                    m.setSizeRange(modification.sizeRange);
                    
                    em.persist(m);
                    em.flush();
                    
                    security.register(m, ResourceUtils.alias(g, "modifications/i", g));
                    
                    params.createParams(m, modification.params);
                }
            }
        }
        
        return g;
    }
    

	@POST
	@Path("/mod/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("user")
	public Response addModification(AddMod modObj) {

		if (session != null){
			Good good = em.find(Good.class, modObj.goodId);

			if (good.getPurchase().getUser().getId() == session.getUser().getId()){
				CopyGroup group = new CopyGroup();
				group.setShouldResetPrimaryKey(true);
				Good modGood = (Good) em.unwrap(JpaEntityManager.class).copy(good, group);

				modGood.setResType(Aspect.EXTENDED);

				modGood.setAlias(String.format("/purchase/%s/good/", modGood.getPurchase().getId()));
				modGood.getPersonalImages().clear();

				em.persist(modGood);
				em.flush();

				modGood.setAlias(String.format("/purchase/%s/good/%s", modGood.getPurchase().getId(), modGood.getId()));
				em.persist(modGood);

				em.merge(modGood);
				em.flush();

				return Response.ok(em.find(Good.class, modGood.getId())).build();
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
	public Response updateGood(Good good) {

		try {
			if (session != null){
				Good oldGood = em.find(Good.class, good.getId());

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
					Company company = em.find(Company.class, good.getCompany().getId());
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

				return Response.ok(em.find(Good.class, good.getId())).build();
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
				Good good = em.find(Good.class, id);

				if (good.getPurchase().getUser().getId() == session.getUser().getId()){
					if (good.getPurchase().getStatus() == Purchase.Status.NEW
							|| good.getPurchase().getStatus() == Purchase.Status.MODERATION){
						good.setStatus(GoodEntity.Good.DELETED);

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

				query.setParameter("status", GoodEntity.Good.DELETED);

				for (Map.Entry<String, List<String>> entry : settedAttributes.entrySet()){
					for (String v : entry.getValue()){
						query = query.setParameter(String.format("cav_%s", entry.getKey()), v);
					}
				}

				List<Good> result = query.getResultList();
				List<AtrGoodList> agl = new ArrayList<>();
				Map<String, AtrGoodList> attributesMap = new HashMap<>();

				for (Good g : result){
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

	@XmlRootElement
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateGoodRequest {
	    
        public String title;
        public String promo;
        
        @XmlElement
        public IParamService.CreateParam[] params;
        
        @XmlElement
        public Modification[] modifications;
        
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Modification {
            
            public float price;
            public float sizeRange;
            
            @XmlElement
            public IParamService.CreateParam[] params;
        }
	}
	
	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateGoodRequest {
        
        public String title;
        public String promo;
        
        @XmlElement
        public IParamService.UpdateParam[] params;
        
        @XmlElement
        public Modification[] modifications;
        
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Modification {
            
            public float price;
            public float sizeRange;
            public Action action;
            
            @XmlElement
            public IParamService.UpdateParam[] params;
        }
    }
	
//	@XmlRootElement
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class CreateModificationRequest {
//        
//        public long purchase;
//        public String title;
//        public String promo;
//        
//        @XmlElement
//        public IParamService.CreateParam[] params;
//    }
	
//	   @Data
//	    @NoArgsConstructor
//	    @AllArgsConstructor
//	    public static class AddObj {
//	        private long pid;
//	    }
//
//	    @Data
//	    @NoArgsConstructor
//	    @AllArgsConstructor
//	    public static class AddMod {
//	        private long goodId;
//	    }
//
//	    @Data
//	    @NoArgsConstructor
//	    @AllArgsConstructor
//	    @XmlRootElement
//	    @XmlAccessorType(XmlAccessType.FIELD)
//	    public static class FilterGoods {
//	        @XmlElement
//	        private List<FilterValue> settedAttributes;
//	        private long pid;
//
//	        private Map<String, List<String>> getArrValuesMap(){
//
//	            Map<String, List<String>> attributes = new HashMap<>();
//	            if (settedAttributes != null){
//	                for (FilterValue val : settedAttributes){
//	                    if (attributes.get(val.getName()) != null){
//	                        attributes.get(val.getName()).add(val.getValue());
//	                    } else {
//	                        attributes.put(val.getName(), new ArrayList<>());
//	                        attributes.get(val.getName()).add(val.getValue());
//	                    }
//	                }
//	            }
//	            return attributes;
//	        }
//	    }
//
//	    @Data
//	    @NoArgsConstructor
//	    @AllArgsConstructor
//	    @XmlRootElement
//	    @XmlAccessorType(XmlAccessType.FIELD)
//	    public static class AtrGoodList implements Serializable {
//	        private static final long serialVersionUID = -6410688391927286753L;
//	        private String attrName;
//	        private String value;
//	        private List<Good> goods;
//	    }
//
//	    @Data
//	    @NoArgsConstructor
//	    @AllArgsConstructor
//	    @XmlRootElement
//	    @XmlAccessorType(XmlAccessType.FIELD)
//	    private static class CatValueList implements Serializable{
//	        private static final long serialVersionUID = -14038014837124724L;
//	        private GoodCategory category;
//	        private List<GoodCategory> childs;
//	    }
//
//	    @Data
//	    @NoArgsConstructor
//	    @AllArgsConstructor
//	    @XmlRootElement
//	    @XmlAccessorType(XmlAccessType.FIELD)
//	    public static class Filtered {
//	        private List<CatValueList> allCategories;
//	        private List<AtrGoodList> goods;
//	    }
}
