package com.ntr1x.treasure.web.resources;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Order;
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.repository.PurchaseRepository;
import com.ntr1x.treasure.web.services.IOrderService;
import com.ntr1x.treasure.web.services.IOrderService.OrdersResponse;
import com.ntr1x.treasure.web.services.IPurchaseService;
import com.ntr1x.treasure.web.services.IPurchaseService.PurchaseCreate;
import com.ntr1x.treasure.web.services.IPurchaseService.PurchaseUpdate;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Purchases")
@Component
@Path("purchases")
public class PurchaseResource {

    @PersistenceContext
    private EntityManager em;
    
//    @Inject
//    private Session session;
    
    @Inject
    private ISecurityService security;
    
	@Inject
	private PurchaseRepository purchases;
	
	@Inject
    private IPurchaseService purchaseService;
	
    @Inject
    private IOrderService orderService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PurchasesResponse list(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size,
        @QueryParam("user") Long user,
        @QueryParam("status") Purchase.Status status
    ) {
        
        // TODO Написать крутой запрос чтобы один на все фильтры
        Page<Purchase> p = null;
        if (status != null && user != null) {
            p = purchases.findByUserIdAndStatus(user, status, new PageRequest(page, size));
        } else if (user != null) {
            p = purchases.findByUserId(user, new PageRequest(page, size));
        }
        
        return new PurchasesResponse(
            p.getTotalElements(),
            page,
            size,
            p.getContent()
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
    @Path("/i/{id}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///purchases/i/{id}:admin" })
    @Transactional
    public OrdersResponse orders(
        @PathParam("purchase") Long purchase,
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size,
        @QueryParam("user") Long user,
        @QueryParam("status") Order.Status status
    ) {
        return orderService.select(page, size, status, user, purchase);
    }
        
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///purchases:admin" })
    @Transactional
    public Purchase create(PurchaseCreate create) {
        
        Purchase purchase = purchaseService.create(create);
        
        security.grant(purchase.getUser(), purchase.getAlias(), "admin");
        
        return purchase;
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///purchases/i/{id}:admin" })
    @Transactional
    public Purchase update(@PathParam("id") long id, PurchaseUpdate update) {
        
        Purchase purchase = purchaseService.update(id, update);
        
        return purchase;
    }
    
    @PUT
    @Path("/i/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///purchases/i/{id}:admin" })
    @Transactional
    public Purchase updateStatus(@PathParam("id") long id, Purchase.Status status) {
        
        Purchase purchase = em.find(Purchase.class, id);
        
        switch (purchase.getStatus()) {
        case NEW:
            if (status == Purchase.Status.MODERATION) {
                purchase.setStatus(status);
            }
            break;
        case MODERATION:
            if (status == Purchase.Status.APPROVED) {
                purchase.setStatus(status);
            }
            break;
        case APPROVED:
            if (status == Purchase.Status.OPEN) {
                purchase.setStatus(status);
            }
            break;
        case OPEN:
            if (EnumSet.of(Purchase.Status.HIDDEN, Purchase.Status.STOPED).contains(status)) {
                purchase.setStatus(status);
            }
            break;
        case HIDDEN:
            if (status == Purchase.Status.OPEN) {
                purchase.setStatus(status);
            }
            break;
        case STOPED:
            if (status == Purchase.Status.PAYMENT) {
                purchase.setStatus(status);
            }
            break;
        case PAYMENT:
            if (status == Purchase.Status.DISTRIBUTION) {
                purchase.setStatus(status);
            }
            break;
        case DISTRIBUTION:
            if (status == Purchase.Status.FINISHED) {
                purchase.setStatus(status);
            }
            break;
        case FINISHED:
            throw new WebApplicationException("Purchase is in the end state", Response.Status.CONFLICT);
        default:
            throw new WebApplicationException("Illegal purchase state", Response.Status.CONFLICT);
        }
        
        return purchase;
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///providers/i/{id}:admin" })
    @Transactional
    public Purchase remove(@PathParam("id") long id) {
        
        Purchase purchase = em.find(Purchase.class, id);

        if (!purchase.getGoods().isEmpty()) {
            throw new WebApplicationException("Purchase in use", Response.Status.CONFLICT);
        }
        
        em.remove(purchase);
        em.flush();
        
        return purchase;
    }

    
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchasesResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Purchase> purchases;
    }
	
//	@Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class LoadList {
//        private long count;
//        private int offset;
//        private int limit;
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class LoadListPurchase {
//        private long count;
//        private int offset;
//        private int limit;
//        private Purchase.Status status;
//    }

    

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class PurchaseResponse {
//        
//        public Purchase purchase;
//        public List<Good> goods;
//        
//        public long count;
//        public int page;
//        public int size;
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @XmlRootElement
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static class AtrValueList implements Serializable{
//        private static final long serialVersionUID = -6410688391927286753L;
//        private Attribute attribute;
//        private List<AttributeValue> values;
//    }
//
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @XmlRootElement
//    public static class PurchaseStatusEdit{
//        public Purchase purchase;
//        public Purchase.Status[] availableStatuses;
//    }
//
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @XmlRootElement
//    public static class PurchaseStatusEditDo{
//        public Purchase.Status status;
//    }
//
//    @XmlRootElement
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class PurchValueList {
//        
//        public Purchase purchase;
//        public List<Order> orders;
//        
//        public float total;
//        public int paidCnt;
//        public int confirmedCnt;
//        public int canceledCnt;
//        public float goodsCnt;
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @XmlRootElement
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static class CatValueList implements Serializable{
//        private static final long serialVersionUID = -14038014837124724L;
//        private GoodCategory category;
//        private List<GoodCategory> childs;
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @XmlRootElement
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static class FilterPurchases {
//        private long count;
//        private int offset;
//        private int limit;
//        private String query;
//        private long cpId;
//        private long pid = 0;
//        private List<String> settedCategories;
//        @XmlElement
//        private List<FilterValue> settedAttributes;
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @XmlRootElement
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static class Filtered {
//        private long count;
//        private int offset;
//        private int limit;
//        private String query;
//        private long cpId;
//        private List<String> settedCategories;
//        private Map<String, List<String>> settedAttributes = new HashMap<>();
//        private List<Good> goods;
//        private List<AtrValueList> attributes;
//        private List<CatValueList> categories;
//    }
}
