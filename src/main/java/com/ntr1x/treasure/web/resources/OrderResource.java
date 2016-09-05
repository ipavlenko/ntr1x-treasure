package com.ntr1x.treasure.web.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.attributes.AttributeEntity;
import com.ntr1x.treasure.web.model.attributes.AttributeValue;
import com.ntr1x.treasure.web.model.purchase.GoodEntity;
import com.ntr1x.treasure.web.model.purchase.OrderEntity;
import com.ntr1x.treasure.web.model.purchase.OrderEntryEntity;
import com.ntr1x.treasure.web.model.purchase.PurchaseEntity;
import com.ntr1x.treasure.web.model.security.SecuritySession;
import com.ntr1x.treasure.web.model.security.SecurityUser;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api("Orders")
@Component
@Path("/ws/order")
public class OrderResource {
	
    @Inject
	private SecuritySession session;

	@PersistenceContext
	private EntityManager em;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class OrdersRequest {
		private int offset;
		private int limit;
		private long pid;
		private String status;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	private static class ConfirmOrderEntryRequest{
		public long id;
	}

//	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
//	@XmlAccessorType(XmlAccessType.FIELD)
	private static class GoodEntrValueList implements Serializable {
		public GoodEntity good;
		public List<EntrValueList> entries;
		public float total;
		public float totalOrders;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	private static class SyntheticOrderEntryEntity{
		public long id;
		public GoodEntity good;
		public Float quantity;
		public boolean confirmed;
		public SecurityUser user;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class EntrValueList implements Serializable {
		private static final long serialVersionUID = 8826219591253863648L;
		private AttributeValue attribute;
		private List<EntrValueListInner> entries;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	public static class EntrValueListInner {
		public AttributeValue attribute;
		public List<SyntheticOrderEntryEntity> entries;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class UpdateRequest {
		private long orderId;
		private OrderEntity.Status status;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class UpdateOrderEntryRequest {
		private OrderEntryEntity entry;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class OrderStat implements Serializable {
		private OrderEntity order;
		private float total;
		private float deliveryPrice;
		private float totalToPay;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class OrdersResponse {
		private long count;
		private int offset;
		private int limit;
		private List<OrderStat> orders;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class BillsResponse {
		private List<GoodEntrValueList> entries;
	}

	/**
	 * Возвращает заказы для текущего покупателя
	 * @param req параметры запроса
	 * @return
	 */
	@POST
	@Path("/items")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response items(OrdersRequest req) {

		try {
			if (session != null){

				int count = em.createNamedQuery("OrderEntity.accessibleUserIdAndStatus", OrderEntity.class)				//TODO
						.setParameter("id", session.getUser().getId())
						.setParameter("status", req.getStatus() != null ? OrderEntity.Status.valueOf(req.getStatus()) : null)
						.getResultList().size();

				List<OrderEntity> orders = em.createNamedQuery("OrderEntity.accessibleUserIdAndStatus", OrderEntity.class)
						.setParameter("id", session.getUser().getId())
						.setParameter("status", req.getStatus() != null ? OrderEntity.Status.valueOf(req.getStatus()) : null)
						.setMaxResults(req.getLimit())
						.setFirstResult(req.getOffset())
						.getResultList();

				List<OrderStat> ordersStats = new ArrayList<>();

				for (OrderEntity o : orders){

					OrderStat stat = new OrderStat();
					stat.setOrder(o);

					for (OrderEntryEntity entry : o.getEntries()){
						stat.setTotal(stat.getTotal() + entry.getQuantity() * entry.getGood().getPrice());
					}

					stat.setDeliveryPrice(Float.parseFloat(o.getDPlace().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("deliveryPrice")).findFirst().get().getValue()));
					stat.setTotalToPay(stat.getTotal() + stat.getDeliveryPrice());		//TODO стоимость доставки

//					o.getEntries().get

					ordersStats.add(stat);
				}

//			return Response.ok(new GenericEntity<List<OrderEntity>>(orders) {}).build();
				return Response.ok(
						new OrdersResponse(
								count,
								req.getOffset(),
								req.getLimit(),
								ordersStats
						)
				).build();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (Exception e){
			throw e;
		}
	}

	/**
	 * Возвращает заказы для текущего селлера и показ в зависимости от статуса
	 * @param req параметры запроса
	 * @return
	 */
	@POST
	@Path("/seller/items")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response sellerItems(OrdersRequest req) {

		if (session != null){

			SecurityUser user = session.getUser();
			if (user.getRole() == SecurityUser.Role.SELLER){

				int count = em.createNamedQuery("OrderEntity.accessibleSellerIdAndPurchaseIdAndStatus", OrderEntity.class)				//TODO
						.setParameter("id", session.getUser().getId())
						.setParameter("pid", req.getPid())
						.setParameter("status", req.getStatus() != null ? OrderEntity.Status.valueOf(req.getStatus()) : null)
						.getResultList().size();

				List<OrderEntity> orders = em.createNamedQuery("OrderEntity.accessibleSellerIdAndPurchaseIdAndStatus", OrderEntity.class)
						.setParameter("id", session.getUser().getId())
						.setParameter("pid", req.getPid())
						.setParameter("status", req.getStatus() != null ? OrderEntity.Status.valueOf(req.getStatus()) : null)
						.setMaxResults(req.getLimit())
						.setFirstResult(req.getOffset())
						.getResultList();

				List<OrderStat> ordersStats = new ArrayList<>();

				for (OrderEntity o : orders){

					OrderStat stat = new OrderStat();
					stat.setOrder(o);

					for (OrderEntryEntity entry : o.getEntries()){
						stat.setTotal(stat.getTotal() + entry.getQuantity() * entry.getGood().getPrice());
					}

					stat.setDeliveryPrice(Float.parseFloat(o.getDPlace().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("deliveryPrice")).findFirst().get().getValue()));
					stat.setTotalToPay(stat.getTotal() + stat.getDeliveryPrice());		//TODO стоимость доставки

					ordersStats.add(stat);
				}

				return Response.ok(
						new OrdersResponse(
								count,
								req.getOffset(),
								req.getLimit(),
								ordersStats
						)
				).build();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
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
	public Response sellerBills(OrdersRequest req) {

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

	@POST
	@Path("/seller/bills/entry/confirm")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("user")
	public Response confirmOrderEntry(ConfirmOrderEntryRequest req) {
		if (session != null){

			SecurityUser user = session.getUser();
			OrderEntryEntity entry = em.find(OrderEntryEntity.class, req.id);

			if (user.getRole() == SecurityUser.Role.SELLER && user.getId() == entry.getOrder().getSeller().getId()){

				entry.setConfirmed(true);

				boolean allConfirmed = true;
				for (OrderEntryEntity entr : entry.getOrder().getEntries()){
					if (!entr.isConfirmed()){
						allConfirmed = false;
						break;
					}
				}

				em.merge(entry);

				if (allConfirmed){
					entry.getOrder().setStatus(OrderEntity.Status.REQUIRES_PAYMENT);
					em.merge(entry.getOrder());
				}

				em.flush();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@DELETE
	@Path("/seller/bills/entry/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response deleteOrderEntry(
			@PathParam("id") long id
	) {
		if (session != null){
			SecurityUser user = session.getUser();
			OrderEntryEntity entry = em.find(OrderEntryEntity.class, id);
			OrderEntity order = entry.getOrder();

			if (order.getStatus() == OrderEntity.Status.NEW){
				if (user.getRole() == SecurityUser.Role.SELLER && user.getId() == entry.getOrder().getSeller().getId()){

					//TODO отправить автосообщение, привязанное к Order, о том, что продавец удалил товар

					// если в заказе был только этот Entry, то отменяем заказ
					if (order.getEntries().size() == 1){
						order.setStatus(OrderEntity.Status.CANCELED);
						em.merge(order);
					}

					em.remove(entry);
					em.flush();

					return Response.ok().build();
				}
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@POST
	@Path("/seller/bills/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("user")
	public Response updateOrderEntry(UpdateOrderEntryRequest req) {

		if (session != null){

			SecurityUser user = session.getUser();

			OrderEntryEntity entry = em.find(OrderEntryEntity.class, req.entry.getId());

			switch (entry.getOrder().getStatus()){
				case NEW:
					if (user.getRole() == SecurityUser.Role.SELLER && user.getId() == entry.getOrder().getSeller().getId()){

						if ((entry.getGood().getQuantity() + entry.getQuantity()) - req.getEntry().getQuantity() > 0){
							entry.getGood().setQuantity(entry.getGood().getQuantity() + entry.getQuantity() - req.getEntry().getQuantity());
							entry.setQuantity(req.getEntry().getQuantity());
						}

						if (req.getEntry().isConfirmed()){
							entry.setConfirmed(true);

							boolean allConfirmed = true;
							for (OrderEntryEntity entr : entry.getOrder().getEntries()){
								if (!entr.isConfirmed()){
									allConfirmed = false;
									break;
								}
							}

							if (allConfirmed){
								entry.getOrder().setStatus(OrderEntity.Status.REQUIRES_PAYMENT);
								em.merge(entry.getOrder());
							}
						}
					}
					else if (user.getRole() == SecurityUser.Role.USER && user.getId() == entry.getOrder().getUser().getId()) {

						if(entry.getGood().getId() != req.getEntry().getGood().getId()){

							GoodEntity good = em.find(GoodEntity.class, req.getEntry().getGood().getId());

							if (good != null){
								if (good.getPurchase().getUser().getId() == entry.getOrder().getSeller().getId()
										&& good.getPurchase().getId() == entry.getGood().getPurchase().getId()
										&& (good.getPurchase().getStatus() == PurchaseEntity.Status.OPEN || good.getPurchase().getStatus() == PurchaseEntity.Status.STOPED)){

									if (good.getQuantity() >= req.getEntry().getQuantity()){

										entry.getGood().setQuantity(entry.getGood().getQuantity() + entry.getQuantity());
										em.merge(entry.getGood());
										em.flush();

										entry.setGood(good);
										good.setQuantity(good.getQuantity() - req.getEntry().getQuantity());
										entry.setQuantity(req.getEntry().getQuantity());
									}
								}
							} else {
								return Response.status(Response.Status.BAD_REQUEST).build();
							}
						} else {
							if ((entry.getGood().getQuantity() + entry.getQuantity()) - req.getEntry().getQuantity() > 0){
								entry.getGood().setQuantity(entry.getGood().getQuantity() + entry.getQuantity() - req.getEntry().getQuantity());
								entry.setQuantity(req.getEntry().getQuantity());
							}
						}

						em.merge(entry);
						em.flush();
					}
					break;
			}

			OrderEntryEntity ordr = em.find(OrderEntryEntity.class, req.entry.getId());

			return Response.ok(new OrderEntryEntity(ordr.getId(), null, ordr.getGood(), ordr.getQuantity(), false)).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("user")
	public Response updateOrder(UpdateRequest req) {

		if (session != null){

			SecurityUser user = session.getUser();

			OrderEntity order = em.createNamedQuery("OrderEntity.accessibleId", OrderEntity.class)
					.setParameter("id", session.getUser().getId())
					.getSingleResult();

			switch (order.getStatus()){
				case NEW:
					if (user.getRole() == SecurityUser.Role.SELLER && user.getId() == order.getSeller().getId()){
						if (req.getStatus() == OrderEntity.Status.REQUIRES_PAYMENT || req.getStatus() == OrderEntity.Status.CANCELED){
							order.setStatus(req.getStatus());
							em.merge(order);
						}
					}
					else if (user.getRole() == SecurityUser.Role.USER && user.getId() == order.getUser().getId()) {
						if (req.getStatus() == OrderEntity.Status.CANCELED) {
							order.setStatus(req.getStatus());
							em.merge(order);
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
						}
					}
					else {
						return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
					}
					break;
				case REQUIRES_PAYMENT:
					if (user.getRole() == SecurityUser.Role.SELLER && user.getId() == order.getSeller().getId()){
						if (req.getStatus() == OrderEntity.Status.CANCELED){
							order.setStatus(req.getStatus());
							em.merge(order);
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
						}
					} else if (user.getRole() == SecurityUser.Role.USER && user.getId() == order.getUser().getId()){
						if (req.getStatus() == OrderEntity.Status.PAID ){
							order.setStatus(req.getStatus());
							order.setPaidDate(new Date());
							em.merge(order);
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
						}
					} else {
						return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
					}
					break;
				case CANCELED:
					return Response.status(Response.Status.UNAUTHORIZED).build();

				case PAID:
					if (user.getRole() == SecurityUser.Role.SELLER && user.getId() == order.getSeller().getId()){
						if (req.getStatus() == OrderEntity.Status.CONFIRMED){
							order.setStatus(req.getStatus());
							order.setConfirmDate(new Date());
							em.merge(order);
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
						}
					} else {
						return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
					}
					break;
				case CONFIRMED:
					if (user.getRole() == SecurityUser.Role.SELLER && user.getId() == order.getSeller().getId() // TODO оставить только DELIVERY (считыватель)
							|| user.getRole() == SecurityUser.Role.DELIVERY && user.getDeliveryPlaces().stream().filter(d -> d.getId() == order.getDPlace().getId()).findFirst().isPresent()){
						if (req.getStatus() == OrderEntity.Status.READY){
							order.setStatus(req.getStatus());
							order.setReadyDate(new Date());
							em.merge(order);
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
						}
					} else {
						return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
					}
					break;
				case READY:
					if (user.getRole() == SecurityUser.Role.USER && user.getId() == order.getUser().getId() // TODO оставить только DELIVERY (считыватель)
							|| user.getRole() == SecurityUser.Role.DELIVERY && user.getDeliveryPlaces().stream().filter(d -> d.getId() == order.getDPlace().getId()).findFirst().isPresent()){
						if (req.getStatus() == OrderEntity.Status.RECEIVED){
							order.setStatus(req.getStatus());
							order.setReceivedDate(new Date());
							em.merge(order);
						} else {
							return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
						}
					} else {
						return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
					}
					break;
				case RECEIVED:
					return Response.status(Response.Status.UNAUTHORIZED).entity("Сurrent user is not unauthorized for this request").build();
			}

			em.flush();
			return Response.ok().build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
}
