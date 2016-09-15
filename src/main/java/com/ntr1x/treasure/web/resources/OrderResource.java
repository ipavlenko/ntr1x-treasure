package com.ntr1x.treasure.web.resources;

import java.time.LocalDateTime;

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

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.p3.Order;
import com.ntr1x.treasure.web.model.p5.OrderEntry;
import com.ntr1x.treasure.web.services.IOrderService;
import com.ntr1x.treasure.web.services.IOrderService.OrdersResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Orders")
@Component
@Path("orders")
public class OrderResource {

    @PersistenceContext
    private EntityManager em;
    
//    @Inject
//	private Session session;

	@Inject
	private IOrderService orderService;
	
	@GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({
	    "res:///orders:admin",
	    "res:///purchases/i/{id}:admin",
	})
    @Transactional
    public OrdersResponse orders(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size,
        @QueryParam("user") Long user,
        @QueryParam("purchase") Long purchase,
        @QueryParam("status") Order.Status status
    ) {
	    
	    return orderService.select(page, size, status, user, purchase);
    }

	/**
	 * Возвращает ведомости по товарам (Заказы, сгруппированные по товарам)
	 * @param req Объект с параметрами запроса
	 * @return
	 */
	@POST
	@Path("/seller/bills/items")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	// TODO: Исправить, запрос некорректно используется
	public Response sellerBills(/*OrdersRequest req*/) {

	    throw new IllegalStateException("Implemented with error");
	    
//		try {
//			if (session != null){
//				List<AttributeEntity> definingAtrs = em.createNamedQuery("Attribute.getDefining", AttributeEntity.class).getResultList();
//
//				String qStr =
//						"	SELECT entr "
//							+ "	FROM OrderEntryEntity entr "
//							+ "	INNER JOIN entr.good g ";
//
//				qStr = req.getPid() > 0 ? String.format(" %s INNER JOIN g.purchase p on p.id = %s ", qStr, req.getPid()) : String.format(" %s INNER JOIN g.purchase p ", qStr);
//				qStr = String.format(" %s INNER JOIN p.user u on u.id = %s ", qStr, session.getUser().getId());
//
//				Query query = em.createQuery(qStr);
//
//				List<OrderEntryEntity> entries = query.getResultList();
//				entries = entries.stream().sorted(OrderEntryEntity.COMPARATOR).collect(Collectors.toList());
//
//				Map<GoodEntity, List<EntrValueList>> goodMap = new HashMap<>();
//
//				if (entries.size() > 0){
//
//					GoodEntity prevGood = entries.get(0).getGood();
//					goodMap.put(prevGood, new ArrayList<>());
//
//					OrderEntryEntity prevEntr = null;
//
//					EntrValueList evl = new EntrValueList();
//					EntrValueListInner evli = new EntrValueListInner();
//
//					for (OrderEntryEntity entr : entries){
//
//						SyntheticOrderEntryEntity sEntr = new SyntheticOrderEntryEntity();
//						sEntr.id = entr.getId();
//						sEntr.good = entr.getGood();
//						sEntr.confirmed = entr.isConfirmed();
//						sEntr.quantity = entr.getQuantity();
//						sEntr.user = entr.getOrder().getUser();
//
//						AttributeValue aVal = entr.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("color")).findFirst().get();
//						AttributeValue aValSz = entr.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("size")).findFirst().get();
//
//						// Проверяем defining attributes
//						for (AttributeValue av : entr.getGood().getAttributes()){
//							// Если аттрибут defining, сравниваем с аттрибутом предыдущего GoodEntity, если они отличаются, складываем в Map новый GoodEntity
//							if (definingAtrs.stream().filter(a -> a.getName().equals(av.getAttribute().getName())).findFirst().isPresent()){
//								if (!prevGood.getAttributes().stream().filter(a -> a.getAttribute().getName().equals(av.getAttribute().getName())).findFirst().get().getValue().equals(av.getValue())){
//									prevGood = entr.getGood();
//									goodMap.put(prevGood, new ArrayList<>());
//									break;
//								}
//							}
//						}
//
//						if(prevEntr == null || !prevEntr.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("color")).findFirst().get().getValue().equals(aVal.getValue())){
//							evl = new EntrValueList();
//							evl.setAttribute(aVal);
//							evl.setEntries(new ArrayList<>());
//							goodMap.get(prevGood).add(evl);
//						}
//
//						// Если размер в entry равен предыдущему
//						if(prevEntr != null && prevEntr.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("size")).findFirst().get().getValue().equals(aValSz.getValue())){
//							evli.entries.add(sEntr);
//						} else {
//							evli = new EntrValueListInner();
//							evli.attribute = aValSz;
//							evli.entries = new ArrayList<>();
//							evli.entries.add(sEntr);
//
//							evl.getEntries().add(evli);
//						}
//
//						prevEntr = entr;
//					}
//				}
//
//				List<GoodEntrValueList> gevl = new ArrayList<>();
//
//				for (Map.Entry<GoodEntity, List<EntrValueList>> entry : goodMap.entrySet()){
//
//					float total = 0f;
//					float totalOrders = 0;
//
//					for (EntrValueList evl : entry.getValue()){
//						for (EntrValueListInner evli : evl.getEntries()){
//							for (SyntheticOrderEntryEntity oee : evli.entries){
//								totalOrders = totalOrders + oee.quantity;
//								total = total + (oee.quantity * oee.good.getPrice());
//							}
//						}
//					}
//
//					gevl.add(new GoodEntrValueList(entry.getKey(), entry.getValue(), total, totalOrders));
//				}
//				return Response.ok(new BillsResponse(gevl)).build();
//			}
//			return Response.status(Response.Status.UNAUTHORIZED).build();
//		} catch (Exception e){
//			log.error("{}", e);
//			throw e;
//		}
	}

	@DELETE
	@Path("/i/{id}/entries/i/{entry}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "res:///orders/i/{id}:admin" })
	@Transactional
	public OrderEntry entriesRemove(@PathParam("id") long id, @PathParam("entry") long entry) {
	    
	    OrderEntry e = em.find(OrderEntry.class, entry);
		Order o = em.find(Order.class, id);
		
		if (e.getOrder().getId() != o.getId()) {
		    throw new WebApplicationException("Entry belongs to another order", Response.Status.CONFLICT);
		}
		
		if (o.getStatus() != Order.Status.NEW) {
		    throw new WebApplicationException("Order status doesn't allow modification", Response.Status.CONFLICT);
		}
		
		em.remove(entry);
        em.flush();
        
        em.refresh(o);
        
        if (o.getEntries().isEmpty()) {
            o.setStatus(Order.Status.CANCELED);
            
            em.merge(o);
            em.flush();
        }
        
        return e;
	}

	@POST
	@Path("/i/{id}/entries/quantity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "res:///orders/i/{id}:admin" })
	@Transactional
	public Response entriesUpdateQuantity(/*UpdateOrderEntryRequest req*/) {
	    
	    return Response.ok().build();
	}

	@PUT
	@Path("/i/{id}/status")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///orders/i/{id}:admin" })
	public Order updateOrderStatus(@PathParam("id") long id, StatusRequest request) {

		Order order = em.find(Order.class, id);

		order.setStatus(request.status);
		
		switch (request.status) {
    		case REQUIRES_PAYMENT:
    		    break;
    		case PAID:
    		    order.setPaid(LocalDateTime.now());
    		    break;
    		case CONFIRMED:
    		    order.setConfirmed(LocalDateTime.now());
    		    break;
    		case READY:
    		    order.setReady(LocalDateTime.now());
    		    break;
    		case RECEIVED:
                order.setReceived(LocalDateTime.now());
                break;
            default:
                // ignore
                break;
		}
		
		em.merge(order);
		em.flush();
		
		return order;
	}
	
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusRequest {
        
        private Order.Status status;
    }
}
