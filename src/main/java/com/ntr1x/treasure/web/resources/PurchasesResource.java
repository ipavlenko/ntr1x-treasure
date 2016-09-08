package com.ntr1x.treasure.web.resources;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.CopyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.CommonConfig;
import com.ntr1x.treasure.web.model.Aspect;
import com.ntr1x.treasure.web.model.Attribute;
import com.ntr1x.treasure.web.model.Document;
import com.ntr1x.treasure.web.model.Good;
import com.ntr1x.treasure.web.model.GoodCategory;
import com.ntr1x.treasure.web.model.Order;
import com.ntr1x.treasure.web.model.OrderEntry;
import com.ntr1x.treasure.web.model.Provider;
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.model.attributes.AttributeValue;
import com.ntr1x.treasure.web.model.security.SecuritySession;
import com.ntr1x.treasure.web.model.security.SecurityUser;
import com.ntr1x.treasure.web.repository.GoodRepository;
import com.ntr1x.treasure.web.repository.OrderRepository;
import com.ntr1x.treasure.web.repository.PurchaseRepository;
import com.ntr1x.treasure.web.utils.map.FilterValue;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api("Purchases")
@Component
@Path("/ws/purchases")
@Slf4j
public class PurchasesResource {

	@Inject
	private SecuritySession session;
	
	@Inject
	private PurchaseRepository purchases;
	
	@Inject
    private OrderRepository orders;
	
	@Inject
	private GoodRepository goods;

    @PersistenceContext
    private EntityManager em;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "auth" })
	@Transactional
	public PurchasesResponse selectWithOrders(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size,
        @QueryParam("status") Purchase.Status status
	){	    
					    
	    Page<Purchase> result = status == null
            ? purchases.findByResTypeAndUserId(Aspect.EXTENDED, session.getUser().getId(), new PageRequest(page, size))
            : purchases.findByResTypeAndStatusAndUserId(Aspect.EXTENDED, status, session.getUser().getId(), new PageRequest(page, size))
	    ;

		List<PurchValueList> purchases = new ArrayList<>();

		for (Purchase p : result) {
			
//		    List<OrderEntity> orders = em.createNamedQuery("OrderEntity.accessibleSellerIdAndPurchaseId", OrderEntity.class)
//					.setParameter("id", session.getUser().getId())
//					.setParameter("pid", p.getId())
//					.getResultList();

		    List<Order> orders = this.orders.findByPurchaseId(p.getId());
		    
			PurchValueList pvl = new PurchValueList(); {
			    
			    pvl.orders = orders;
	            pvl.purchase = p;
			}

			for (Order order : orders) {
			    
				for (OrderEntry entr : order.getEntries()){

					pvl.goodsCnt += entr.getQuantity();
					pvl.total += entr.getQuantity() * entr.getGood().getPrice();

					if (order.getStatus() == Order.Status.PAID){
						pvl.paidCnt += 1;
					} else if (order.getStatus() == Order.Status.CONFIRMED){
						pvl.confirmedCnt += 1;
					} else if (order.getStatus() == Order.Status.CANCELED){
						pvl.canceledCnt += 1;
					}
				}
			}

			purchases.add(pvl);
		}

		return new PurchasesResponse(
			result.getTotalElements(),
			page,
			size,
			purchases
		);
	}

	@GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Purchase select(@PathParam("id") long id) {
        
	    Purchase purchase = em.find(Purchase.class, id);
        return purchase;
    }
	
	@GET
    @Path("/i/{id}/goods")
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "res:///purchases/i/{id}:admin" })
    @Transactional
    public GoodsResponse selectGoods(
        @PathParam("id") long id,
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size
    ) {
        
	    Page<Good> result = goods.findByPurchaseId(id, new PageRequest(page, size));
        return new GoodsResponse(
            result.getTotalElements(),
            page,
            size,
            result.getContent()
        );
    }

//	private static Map<String, List<String>> getArrValuesMap(List<FilterValue> lst){
//
//		Map<String, List<String>> settedAttributes = new HashMap<>();
//		if (lst != null){
//			for (FilterValue val : lst){
//				if (settedAttributes.get(val.getName()) != null){
//					settedAttributes.get(val.getName()).add(val.getValue());
//				} else {
//					settedAttributes.put(val.getName(), new ArrayList<>());
//					settedAttributes.get(val.getName()).add(val.getValue());
//				}
//			}
//		}
//		return settedAttributes;
//	}

	@POST
	@Path("/filter/purchases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Filtered doFilter(
			FilterPurchases filter
	) throws Exception{
		try {
			Map<String, List<String>> settedAttributes = getArrValuesMap(filter.getSettedAttributes());

			int match = 0;

			String query = ""
					+ " DROP TEMPORARY TABLE IF EXISTS `temp_good`; "
					+ " DROP TEMPORARY TABLE IF EXISTS `temp_selection_cnt`; "
					+ " DROP TEMPORARY TABLE IF EXISTS `temp_selection`; "
					+ " DROP TEMPORARY TABLE IF EXISTS `temp_category`; "
					+ " DROP TEMPORARY TABLE IF EXISTS `temp_attribute_value`; "

					+ " CREATE TEMPORARY TABLE `temp_good` AS ( "
					+ "  SELECT "
					+ "    u.Id, "
					+ "    sum(u.`Matches`) `Matches` "
					+ "  FROM ( "
					+ "    SELECT "
					+ "      av.RelatesId `Id`, "
					+ "    count(*) `Matches` "
					+ "    FROM core_attribute_value av ";

			if (	settedAttributes.size() > 0
					|| filter.getQuery() != null && filter.getQuery().length() > 0
					|| (filter.getSettedCategories() != null && filter.getSettedCategories().size() > 0)
					|| filter.getCpId() > 0){
				query = String.format(" %s INNER JOIN core_attribute a ON (av.AttributeId = a.Id AND (1 = 0 ", query);
			}
			else {
				query = String.format(" %s INNER JOIN core_attribute a ON (av.AttributeId = a.Id AND (1 = 1 ", query);
			}

			for (Map.Entry<String, List<String>> entry : settedAttributes.entrySet()){
				query = String.format("%s OR (a.`Name` = ? AND av.`Value` = ?)", query);
				match ++;
			}

			if (filter.getQuery() != null && filter.getQuery().length() > 0){
				query = String.format(" %s OR (a.`Name` = 'title' AND av.`Value` LIKE (?)) ", query);
				match ++;
			}
			query = String.format(" %s )) ", query);

			if (filter.getPid() > 0){
				query = String.format(" %s INNER JOIN core_good g ON (g.ResourceId = av.RelatesId AND (%s = g.PurchaseId) AND ('%s' = g.Status)) ", query, filter.getPid(), GoodEntity.Good.ACTIVE.name());
			} else {
				query = String.format(" %s INNER JOIN core_good g ON (g.ResourceId = av.RelatesId AND ('%s' = g.Status)) ", query, GoodEntity.Good.ACTIVE.name());
			}

			query = String.format(
							" %s GROUP BY av.RelatesId "
							+ "    UNION ALL"
							+ "    SELECT "
							+ "      gcm.GoodId `Id`, "
							+ "      count(*) `Matches` "
							+ "    FROM core_good_categories_map gcm ",
					query);

			if (filter.getSettedCategories() != null && filter.getSettedCategories().size() > 0){

				query = String.format(" %s INNER JOIN core_good_categories gc ON (gcm.CategoryId = gc.Id AND (1 = 0 OR (gc.`Name` IN ( ", query);
				for (String ignored : filter.getSettedCategories()){
					query = String.format(" %s ?,", query);
				}

				query = query.substring(0, query.length()-1);
				query = String.format(" %s )) ", query);

				match ++;
			} else if (filter.getCpId() > 0){
				query = String.format(" %s INNER JOIN core_good_categories gc ON (gcm.CategoryId = gc.Id AND (1 = 0 OR (gc.`Id` = ?) ", query);

				match ++;
			} else if (settedAttributes.size() > 0 || filter.getQuery() != null && filter.getQuery().length() > 0){
				query = String.format(" %s INNER JOIN core_good_categories gc ON (gcm.CategoryId = gc.Id AND (1 = 0 ", query);
			} else {
				query = String.format(" %s INNER JOIN core_good_categories gc ON (gcm.CategoryId = gc.Id AND (1 = 1 ", query);
			}

			query = String.format(" %s )) ", query);

			if (filter.getPid() > 0){
				query = String.format(" %s INNER JOIN core_good g ON (g.ResourceId = gcm.GoodId AND (%s = g.PurchaseId) AND ('%s' = g.Status)) ", query, filter.getPid(), GoodEntity.Good.ACTIVE.name());
			} else{
				query = String.format(" %s INNER JOIN core_good g ON (g.ResourceId = gcm.GoodId AND ('%s' = g.Status)) ", query, GoodEntity.Good.ACTIVE.name());
			}

			if (	settedAttributes.size() > 0
					|| (filter.getQuery() != null && (filter.getQuery().length() + settedAttributes.size()) > 1)
					|| (filter.getSettedCategories() != null && filter.getSettedCategories().size() > 0)
					|| filter.getCpId() > 0){

				query = String.format(" %s GROUP BY gcm.GoodId ) u GROUP BY `Id` HAVING `Matches` = %s); ",query,match);
			} else {
				query = String.format(" %s GROUP BY gcm.GoodId ) u GROUP BY `Id`); ",query);
			}

			query = String.format(
					" %s CREATE TEMPORARY TABLE `temp_selection_cnt` AS ( "
							+ " SELECT DISTINCT g.* "
							+ "  FROM `temp_good` m "
							+ "  INNER JOIN core_good g ON g.ResourceId = m.Id "
//						+ "  INNER JOIN core_purchase p ON (p.ResourceId = g.PurchaseId AND g.PurchaseId = ? ) "

							+ "  INNER JOIN `core_attribute_value` cav1 ON cav1.`RelatesId` = g.`ResourceId` "
							+ "  INNER JOIN `core_attribute` ca1 ON ca1.`Id` = cav1.`AttributeId` AND ca1.`Name` = 'brand' "
							+ "  INNER JOIN `core_attribute_value` cav2 ON cav2.`RelatesId` = g.`ResourceId` "
							+ "  INNER JOIN `core_attribute` ca2 ON ca2.`Id` = cav2.`AttributeId` AND ca2.`Name` = 'title' "
							+ "  INNER JOIN `core_attribute_value` cav3 ON cav3.`RelatesId` = g.`ResourceId` "
							+ "  INNER JOIN `core_attribute` ca3 ON ca3.`Id` = cav3.`AttributeId` AND ca3.`Name` = 'desc' "
							+ "  INNER JOIN core_purchase p ON p.ResourceId = g.PurchaseId "
					, query);

			if (filter.getPid() > 0){
				query = String.format(" %s GROUP BY cav1.`Value`, cav2.`Value`, cav3.`Value` ); ", query);
			} else {
				query = String.format(" %s GROUP BY p.ResourceId ); ", query);
			}

			query = String.format(
					" %s SELECT count(*) from temp_selection_cnt; "

					+ " CREATE TEMPORARY TABLE `temp_selection` AS ( "
					+ "  SELECT DISTINCT "
					+ "    g.* "
					+ "  FROM `temp_good` m "
					+ "  INNER JOIN core_good g ON g.ResourceId = m.Id "

					+ "  INNER JOIN `core_attribute_value` cav1 ON cav1.`RelatesId` = g.`ResourceId` "
					+ "  INNER JOIN `core_attribute` ca1 ON ca1.`Id` = cav1.`AttributeId` AND ca1.`Name` = 'brand' "
					+ "  INNER JOIN `core_attribute_value` cav2 ON cav2.`RelatesId` = g.`ResourceId` "
					+ "  INNER JOIN `core_attribute` ca2 ON ca2.`Id` = cav2.`AttributeId` AND ca2.`Name` = 'title' "
					+ "  INNER JOIN `core_attribute_value` cav3 ON cav3.`RelatesId` = g.`ResourceId` "
					+ "  INNER JOIN `core_attribute` ca3 ON ca3.`Id` = cav3.`AttributeId` AND ca3.`Name` = 'desc' "
					+ "  INNER JOIN core_purchase p ON p.ResourceId = g.PurchaseId "
					, query);

			if (filter.getPid() > 0){
				query = String.format(" %s GROUP BY cav1.`Value`, cav2.`Value`, cav3.`Value` ", query);
			} else {
				query = String.format(" %s GROUP BY p.ResourceId ", query);
			}

			query = String.format(" %s LIMIT %s OFFSET %s  ); ",
					query,
					filter.getLimit() > 0 ? filter.getLimit() : CommonConfig.PAGE_ITEMS_LIMIT,
					filter.getOffset()
			);

			query = String.format(
					" %s SELECT DISTINCT * "
					+ " FROM `temp_selection` s "
					+ " ; "

					+ " CREATE TEMPORARY TABLE `temp_attribute_value` AS ( "
					+ "  SELECT DISTINCT "
					+ "    av.`Id`, "
					+ "    av.`Value`, "
					+ "    av.`RelatesId`, "
					+ "    av.`AttributeId` "
					+ "  FROM `temp_good` m "
					+ "  INNER JOIN core_attribute_value av ON av.RelatesId = m.Id "
					+ "  INNER JOIN core_attribute a ON a.Id = av.AttributeId AND a.Filtering = TRUE "
					+ "  INNER JOIN core_good g ON g.ResourceId = av.`RelatesId` "
					+ "  GROUP BY av.`Value` "
					+ " ); "
					+ " SELECT "
					+ "  av.* "
					+ " FROM `temp_attribute_value` av "
					+ " ; "

					+ " CREATE TEMPORARY TABLE `temp_category` AS ( "
					+ "  SELECT DISTINCT "
					+ "    u.* "
					+ "  FROM ( "
					+ "  SELECT DISTINCT "
					+ "      gc1.* "
					+ "    FROM `temp_good` m "
					+ "    INNER JOIN `core_good_categories_map` gcm ON gcm.GoodId = m.Id "
					+ "    INNER JOIN `core_good_categories` gc1 ON (gc1.ParentId IS NULL OR gc1.Id = gcm.CategoryId) "
					+ "    UNION "
					+ "  SELECT "
					+ "   gc2.* "
					+ "  FROM core_good_categories gc2 "
					+ "    WHERE gc2.ParentId IS NULL "
					+ "  ) u "
					+ " ); "
					+ " SELECT * "
					+ " FROM `temp_category` "
					+ " ; "

					+ " DROP TEMPORARY TABLE `temp_selection`; "
					+ " DROP TEMPORARY TABLE `temp_selection_cnt`; "
					+ " DROP TEMPORARY TABLE `temp_category`; "
					+ " DROP TEMPORARY TABLE `temp_attribute_value`; "
					+ " DROP TEMPORARY TABLE `temp_good`; "
					, query);

			Connection con = em.unwrap(Connection.class);
			PreparedStatement stmt = con.prepareStatement(query);

			int index = 1;
			for (Map.Entry<String, List<String>> entry : settedAttributes.entrySet()){
				stmt.setString(index, entry.getKey());
				index ++;

				stmt.setString(index, entry.getValue().get(0));
				index ++;
			}

			if (filter.getQuery() != null && filter.getQuery().length() > 0){
				stmt.setString(index, "%" + filter.getQuery() + "%");
				index ++;
			}

			if (filter.getSettedCategories() != null && filter.getSettedCategories().size() > 0){
				for (String prm : filter.getSettedCategories()){
					stmt.setString(index, prm);
					index ++;
				}
			} else {
				if (filter.getCpId() > 0){
					stmt.setLong(index, filter.getCpId());
					index ++;
				}
			}

//			if (filter.getPid() > 0){
//				stmt.setLong(index, filter.getPid());
//				index ++;
//				stmt.setLong(index, filter.getPid());
//			}

			stmt.execute();
			stmt.getMoreResults();
			stmt.getMoreResults();
			stmt.getMoreResults();
			stmt.getMoreResults();
			stmt.getMoreResults();
			stmt.getMoreResults();

			stmt.getMoreResults();
			ResultSet rs = stmt.getResultSet();

			rs.next();

			int count = rs.getInt(1);

			stmt.getMoreResults();
			stmt.getMoreResults();

			rs = stmt.getResultSet();

			List<Good> goods = new ArrayList<>();

			while(rs.next()) {
				goods.add(em.find(Good.class, rs.getLong("ResourceId")));
			}

			for (Good g : goods){
				g.getPurchase();
			}

			stmt.getMoreResults();
			stmt.getMoreResults();
			rs = stmt.getResultSet();

			List<AttributeValue> at = new ArrayList<>();

			while(rs.next()) {
				at.add(em.find(AttributeValue.class, rs.getLong("Id")));
			}

			stmt.getMoreResults();
			stmt.getMoreResults();
			rs = stmt.getResultSet();

			List<GoodCategory> categories = new ArrayList<>();

			while(rs.next()) {
				categories.add(em.find(GoodCategory.class, rs.getLong("Id")));
			}

			List<AtrValueList> avl = new ArrayList<>();
			Map<Attribute, List<AttributeValue>> attributesMap = new HashMap<>();

			for (AttributeValue atr : at){
				if (attributesMap.get(atr.getAttribute()) != null){
					attributesMap.get(atr.getAttribute()).add(atr);
				} else {
					attributesMap.put(atr.getAttribute(), new ArrayList<>());
					attributesMap.get(atr.getAttribute()).add(atr);
				}
			}

			for (Map.Entry<Attribute, List<AttributeValue>> entry : attributesMap.entrySet()){
				avl.add(new AtrValueList(entry.getKey(), entry.getValue()));
			}

			Collections.sort(avl, (object1, object2) -> object1.getAttribute().getName().compareTo(object2.getAttribute().getName()));

			List<CatValueList> cvl = new ArrayList<>();

			Map<GoodCategory, List<GoodCategory>> catMap = new HashMap<>();

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

			return new Filtered(
					count,
					filter.getOffset(),
					filter.getLimit(),
					filter.query,
					filter.getCpId(),
					filter.getSettedCategories(),
					settedAttributes,
					goods,
					avl,
					cvl
			);
		} catch (Exception e){
			log.error("{}", e);
			throw e;
		}
	}

//	@GET
//	@Path("/items/{id}/status/edit")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public Response purchaseStatusEdit(
//			@PathParam("id") long id
//	) {
//		if (session != null){
//			SecurityUser user = session.getUser();
//			if (user.getRole() == SecurityUser.Role.SELLER || user.getRole() == SecurityUser.Role.MODERATOR){
//
//				PurchaseEntity purchase = em.find(PurchaseEntity.class, id);
//
//				switch (purchase.getStatus()){
//					case NEW:
//						if (user.getId() == purchase.getUser().getId()){
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{PurchaseEntity.Status.MODERATION})).build();
//						} else {
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{})).build();
//						}
//					case MODERATION:
//						if (user.getRole() == SecurityUser.Role.MODERATOR){
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{PurchaseEntity.Status.APPROVED})).build();
//						} else {
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{})).build();
//						}
//					case APPROVED:
//						if (user.getId() == purchase.getUser().getId()){
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{PurchaseEntity.Status.OPEN})).build();
//						} else {
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{})).build();
//						}
//					case OPEN:
//						if (user.getId() == purchase.getUser().getId() && purchase.getStopDate().after(new Date())){
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{PurchaseEntity.Status.HIDDEN, PurchaseEntity.Status.STOPED})).build();
//						} else {
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{})).build();
//						}
//					case HIDDEN:
//						if (user.getId() == purchase.getUser().getId()){
//							if (purchase.getStopDate().after(new Date())){
//								return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{PurchaseEntity.Status.OPEN})).build();
//							} else{
//								return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{PurchaseEntity.Status.STOPED})).build();
//							}
//						}
//						return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{})).build();
//					case STOPED:
//						if (user.getId() == purchase.getUser().getId()){
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{PurchaseEntity.Status.PAYMENT})).build();
//						}
//						return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{})).build();
//					case PAYMENT:
//						if (user.getId() == purchase.getUser().getId()){
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{PurchaseEntity.Status.DISTRIBUTION})).build();
//						}
//						return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{})).build();
//					case DISTRIBUTION:
//						if (user.getId() == purchase.getUser().getId()){
//							return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{PurchaseEntity.Status.FINISHED})).build();
//						}
//						return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{})).build();
//					default:
//						return Response.ok(new PurchaseStatusEdit(purchase, new PurchaseEntity.Status[]{})).build();
//				}
//			}
//			return Response.status(Response.Status.UNAUTHORIZED).build();
//		}
//		return Response.status(Response.Status.UNAUTHORIZED).build();
//	}

	@POST
	@Path("/items/{id}/status/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("seller")
	public Response purchaseStatusUpdate(
			@PathParam("id") long id,
			PurchaseStatusEditDo req
	) {
		if (session != null){
			SecurityUser user = session.getUser();
			if (user.getRole() == SecurityUser.Role.SELLER){
				Purchase purchase = em.find(Purchase.class, id);

				switch (req.status){
					case MODERATION:
						if (user.getId() == purchase.getUser().getId()){
							if (purchase.getStatus() == Purchase.Status.NEW){
								purchase.setStatus(req.status);
							} else {
								return Response.status(Response.Status.UNAUTHORIZED).build();
							}
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).build();
						}
						break;
					case APPROVED:
						if (user.getRole() == SecurityUser.Role.MODERATOR){
							if (purchase.getStatus() == Purchase.Status.MODERATION){
								purchase.setStatus(req.status);
							} else {
								return Response.status(Response.Status.UNAUTHORIZED).build();
							}
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).build();
						}
						break;
					case OPEN:
						if (user.getId() == purchase.getUser().getId()){
							if ((purchase.getStatus() == Purchase.Status.APPROVED || purchase.getStatus() == Purchase.Status.HIDDEN) && purchase.getStopDate().after(new Date())){
								purchase.setStatus(req.status);
							} else {
								return Response.status(Response.Status.UNAUTHORIZED).build();
							}
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).build();
						}
						break;
					case HIDDEN:
						if (user.getId() == purchase.getUser().getId()){
							if (purchase.getStatus() == Purchase.Status.OPEN && purchase.getStopDate().after(new Date())){
								purchase.setStatus(req.status);
							} else {
								return Response.status(Response.Status.UNAUTHORIZED).build();
							}
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).build();
						}
						break;
					case STOPED:
						if (user.getId() == purchase.getUser().getId()){
							if (purchase.getStatus() == Purchase.Status.OPEN){
								purchase.setStatus(req.status);
							} else {
								return Response.status(Response.Status.UNAUTHORIZED).build();
							}
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).build();
						}
						break;
					case PAYMENT:
						if (user.getId() == purchase.getUser().getId()){
							if (purchase.getStatus() == Purchase.Status.STOPED){
								purchase.setStatus(req.status);
							} else {
								return Response.status(Response.Status.UNAUTHORIZED).build();
							}
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).build();
						}
						break;
					case DISTRIBUTION:
						if (user.getId() == purchase.getUser().getId()){
							if (purchase.getStatus() == Purchase.Status.PAYMENT){
								purchase.setStatus(req.status);
							} else {
								return Response.status(Response.Status.UNAUTHORIZED).build();
							}
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).build();
						}
						break;
				}
				return Response.ok().build();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@GET
	@Path("/provider/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("seller")
	public Response addProvider() {
		if (session != null){

			Provider provider = new Provider();
			provider.setTitle("");
			provider.setPromo("");
			provider.setDesc("");
			provider.setResType(Aspect.EXTENDED);
			provider.setAlias(String.format("/user/%s/provider/", session.getUser().getId()));
			provider.setUser(session.getUser());

			em.persist(provider);
			em.flush();

			provider.setAlias(String.format("/user/%s/provider/%s", session.getUser().getId(), provider.getId()));
			em.persist(provider);
			em.flush();

			return Response.ok(em.find(Provider.class, provider.getId())).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@POST
	@Path("/providers/items")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("seller")
	public Response listProviders(LoadListProvider prm) {
		try {
			if (session != null){



//				int count = em.createNamedQuery("ProviderEntity.accessibleOfUserId", ProviderEntity.class)		//TODO setHint SELECT FOUND_ROWS()
//						.setParameter("id", session.getUser().getId())
//						.getResultList().size();


				List<Provider> providers = em.createNamedQuery("ProviderEntity.accessibleOfUserId", Provider.class)
						.setParameter("id", session.getUser().getId())
//						.setMaxResults(prm.getLimit())
//						.setFirstResult(prm.getLimit())
						.getResultList();

				if (prm.getStatus() != null && prm.getStatus() == Provider.Status.NEW){
					providers.removeIf(p -> p.getPurchases().size() > 0);
				}

//				return Response.ok(new GenericEntity<List<ProviderEntity>>(p) {}).build();
				return Response.ok(new Providers(
//						count,
//						prm.getOffset(),
//						prm.getLimit(),
						providers)
				).build();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (Exception e){
			throw e;
		}
	}

	@GET
	@Path("/provider/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("seller")
	public Response getProvider(
			@PathParam("id") long id
	) {
		if (session != null){

			return Response.ok(
					em.createNamedQuery("ProviderEntity.accessibleOfIdUserId", Provider.class)
							.setParameter("uid", session.getUser().getId())
							.setParameter("pid", id)
							.getSingleResult()
			).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@POST
	@Path("/provider/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response updateProvider(Provider provider) {
		if (session != null){

			List<Provider> providers = em.createNamedQuery("ProviderEntity.accessibleOfUserId", Provider.class)
					.setParameter("id", session.getUser().getId())
					.getResultList();

			if((providers.stream().filter(p -> p.getId() == provider.getId()).findFirst().isPresent())) {
				Provider oldProvider = em.find(Provider.class, provider.getId());

				oldProvider.setTitle(provider.getTitle());
				oldProvider.setPromo(provider.getPromo());
				oldProvider.setDesc(provider.getDesc());

				for (AttributeValue val : provider.getAttributes()){
					oldProvider.getAttributes().stream().filter(p -> p.getAttribute().getName().equals(val.getAttribute().getName())).findFirst().get().setValue(val.getValue());
				}

				em.merge(oldProvider);
				em.flush();

				return Response.ok(em.find(Provider.class, provider.getId())).build();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@DELETE
	@Path("/provider/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response deleteProvider(
			@PathParam("id") long id
	) {
		Provider provider = em.find(Provider.class, id);

		if (provider.getPurchases() != null && provider.getPurchases().size() >0){
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		em.remove(provider);
		return Response.ok().build();
	}

    /**
     * Создает поставку(закупку) с предустановленными опциями по умолчанию
     * @return Созданный объект для последующего редактирования и финального сохранения
     */
    @GET
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
	//	@RolesAllowed("seller")
    public Response addPurchase() {

		if (session != null){
			try {

				SecurityUser user = session.getUser();

				CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
				CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
				Root res = criteriaQuery.from(Purchase.class);
				criteriaQuery.where(criteriaBuilder.equal(res.get("resType"), Aspect.ROOT_PURCHASE));

				Purchase basePurchase = (Purchase) em.createQuery(criteriaQuery).getSingleResult();

				CopyGroup group = new CopyGroup();
				group.setShouldResetPrimaryKey(true);
				Purchase newPurchase = (Purchase) em.unwrap(JpaEntityManager.class).copy(basePurchase, group);

				newPurchase.setResType(Aspect.EXTENDED);

				newPurchase.setTitle("Новая закупка");
				newPurchase.setAlias("/purchase/");

				newPurchase.setUser(user);

				em.persist(newPurchase);

				newPurchase.setStatus(Purchase.Status.NEW);
//				newPurchase.setGoods(new ArrayList<>());
				newPurchase.setComments(new ArrayList<>());
				newPurchase.setLikes(new ArrayList<>());
				newPurchase.setDocuments(new ArrayList<>());
				newPurchase.setImages(new ArrayList<>());

				em.persist(newPurchase);
				em.flush();

				newPurchase.setAlias(String.format("/purchase/%s", newPurchase.getId()));
				em.persist(newPurchase);

				em.flush();

				return Response.ok(newPurchase).build();
			} catch (Throwable th){
				throw th;
			}
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
    }

//    @POST
//    @Path("/create")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public PurchaseEntity createPurchase(PurchaseEntity entity) {
//
//        try {
//            em.persist(entity);
//
//            for (GoodEntity good : entity.getGoods()){
//                em.persist(good);
//            }
//
//            em.persist(entity);
//            em.flush();
//
//            entity.setName(String.format("/purchase/%s", entity.getId()));
//            em.persist(entity);
//
//            for (GoodEntity good : entity.getGoods()){
//                good.setName(String.format("/purchase/%s/good/%s", entity.getId(), good.getId()));
//                em.persist(good);
//            }
//
//            em.flush();
//
//            //TODO добавить физически файлы:
//
//        } catch (Throwable th) {
//            throw th;
//        }
//
//        return entity;
//    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updatePurchase(Purchase purchase) {

		//TODO проверка purchases через @RolesAllowed (о том, что Purchase принадлежит User)

		if (session == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		try {
			SecurityUser user = session.getUser();		//TODO

			List<Provider> providers = em.createNamedQuery("ProviderEntity.accessibleOfUserId", Provider.class)
					.setParameter("id", user.getId())
					.getResultList();

			if(purchase.getProvider() != null && !providers.stream().filter(p -> p.getId() == purchase.getProvider().getId()).findFirst().isPresent()) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}

			Purchase purchaseOld = em.find(Purchase.class, purchase.getId());

			if (purchase.getProvider() != null ){
				Provider newProvider = em.find(Provider.class, purchase.getProvider().getId());
				if (newProvider.getUser().getId() == user.getId()){
					purchaseOld.setProvider(em.find(Provider.class, purchase.getProvider().getId()));
				}
				return Response.status(Response.Status.UNAUTHORIZED).entity("Current user does not own the setted provider").build();
			}

			purchaseOld.setTitle(purchase.getTitle());

			purchaseOld.setOpenDate(purchase.getOpenDate());
			purchaseOld.setStopDate(purchase.getStopDate());
			purchaseOld.setStartDeliveryDate(purchase.getStartDeliveryDate());
			purchaseOld.setNextDeliveryExpectDate(purchase.getNextDeliveryExpectDate());


//				if (purchaseOld.getStatus() != PurchaseEntity.Status.PAYMENT					//TODO вернуть проверку
//						&& purchaseOld.getStatus() != PurchaseEntity.Status.DISTRIBUTION
//						&& purchaseOld.getStatus() != PurchaseEntity.Status.FINISHED
//						){

			for (AttributeValue val : purchase.getAttributes()){
				purchaseOld.getAttributes().stream().filter(p -> p.getAttribute().getName().equals(val.getAttribute().getName())).findFirst().get().setValue(val.getValue());

				if (val.getAttribute().getName().equals("title")){
					purchaseOld.setTitle(val.getValue());
				}
			}

			purchaseOld.setImages(purchase.getImages());

			for (Document doc : purchase.getDocuments()){
				if((doc.getId() > 0) &&  !purchaseOld.getDocuments().stream().filter(d -> d.getId() == doc.getId()).findFirst().isPresent()) {
					return Response.status(Response.Status.UNAUTHORIZED).build();
				}
			}
			purchaseOld.setDocuments(purchase.getDocuments());

			em.merge(purchaseOld);
			em.flush();

			/*
				for (GoodEntity good : purchase.getGoods()) {

					// проверка на возможный угон Good
					if((good.getId() > 0) &&  !purchaseOld.getGoods().stream().filter(g -> g.getId() == good.getId()).findFirst().isPresent()) {
						return Response.status(Response.Status.UNAUTHORIZED).build();
					}

					if (good.getAction() != null){
						switch(good.getAction()) {
							case REMOVE:
								// TODO You should check if the parent purchase is
//								em.remove(em.find(GoodEntity.class, good.getId()));
								GoodEntity delGood = em.find(GoodEntity.class, good.getId());

								if (delGood.getPurchase().getStatus() == PurchaseEntity.Status.NEW
										|| delGood.getPurchase().getStatus() == PurchaseEntity.Status.MODERATION){
									delGood.setStatus(GoodEntity.Status.DELETED);

									em.merge(delGood);
									em.flush();
								}
								else {
									return Response.status(Response.Status.BAD_REQUEST).build();
								}

								break;
							case UPDATE:
								GoodEntity oldGood = purchaseOld.getGoods().stream().filter(g -> g.getId() == good.getId()).findFirst().get();

								for (AttributeValue val : good.getAttributes()){
									oldGood.getAttributes().stream().filter(g -> g.getAttribute().getName().equals(val.getAttribute().getName())).findFirst().get().setValue(val.getValue());
								}

								oldGood.setPrice(good.getPrice());
								oldGood.setQuantity(good.getQuantity());
								oldGood.setImages(good.getImages());
								oldGood.setPersonalImages(good.getPersonalImages());
								oldGood.setTitle(good.getTitle());

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
								break;
						}
					}
				}
			*/

				em.flush();
//				}

			Purchase p = em.find(Purchase.class, purchase.getId());

			return Response.ok(em.find(Purchase.class, purchase.getId())).build();
		} catch (Exception e){
			log.error("{}", e);
			throw e;
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response itemsDelete(@PathParam("id") long id) {
	    
	    Purchase entity = em.find(Purchase.class, id);
        
        List<Good> goods = entity.getGoods();

        if (goods != null && !goods.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        em.remove(entity);
        em.flush();

        return Response.ok().build();
	}
	
	@Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoadList {
        private long count;
        private int offset;
        private int limit;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoadListProvider {
//	      private long count;
//	      private int offset;
//	      private int limit;
        private Provider.Status status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoadListPurchase {
        private long count;
        private int offset;
        private int limit;
        private Purchase.Status status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchasesResponse {
        
        private long count;
        private int page;
        private int size;
        private List<PurchValueList> purchases;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsResponse {
        
        private long count;
        private int page;
        private int size;
        private List<Good> goods;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Providers {
//	      private long count;
//	      private int offset;
//	      private int limit;
        private List<Provider> providers;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AtrValueList implements Serializable{
        private static final long serialVersionUID = -6410688391927286753L;
        private Attribute attribute;
        private List<AttributeValue> values;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement
    public static class PurchaseStatusEdit{
        public Purchase purchase;
        public Purchase.Status[] availableStatuses;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement
    public static class PurchaseStatusEditDo{
        public Purchase.Status status;
    }

    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchValueList {
        
        public Purchase purchase;
        public List<Order> orders;
        
        public float total;
        public int paidCnt;
        public int confirmedCnt;
        public int canceledCnt;
        public float goodsCnt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CatValueList implements Serializable{
        private static final long serialVersionUID = -14038014837124724L;
        private GoodCategory category;
        private List<GoodCategory> childs;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FilterPurchases {
        private long count;
        private int offset;
        private int limit;
        private String query;
        private long cpId;
        private long pid = 0;
        private List<String> settedCategories;
        @XmlElement
        private List<FilterValue> settedAttributes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Filtered {
        private long count;
        private int offset;
        private int limit;
        private String query;
        private long cpId;
        private List<String> settedCategories;
        private Map<String, List<String>> settedAttributes = new HashMap<>();
        private List<Good> goods;
        private List<AtrValueList> attributes;
        private List<CatValueList> categories;
    }
}
