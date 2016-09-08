package com.ntr1x.treasure.web.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Aspect;
import com.ntr1x.treasure.web.model.Cart;
import com.ntr1x.treasure.web.model.CartEntry;
import com.ntr1x.treasure.web.model.Depot;
import com.ntr1x.treasure.web.model.Good;
import com.ntr1x.treasure.web.model.Order;
import com.ntr1x.treasure.web.model.OrderEntry;
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.model.security.SecuritySession;
import com.ntr1x.treasure.web.model.security.SecurityUser;
import com.ntr1x.treasure.web.repository.DepotRepository;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Api("Carts")
@Component
@Path("/ws/cart")
@PermitAll
public class CartResource {

	@Inject
	private SecuritySession session;

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private DepotRepository stores;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "auth" })
    @Transactional
    public Cart select() {
	    return session.getUser().getCart();
    }

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response addToCart(CartObject add) {
		if (session != null){
			if (add.getQuantity() > 0){
				SecurityUser user = session.getUser();

				Good good = em.find(Good.class, add.getGoodId());

				if (good == null){
					return Response.status(Response.Status.BAD_REQUEST).entity("Good not found").build();
				}

				if (good.getQuantity() >= add.getQuantity()){
					if (good.getPurchase().getStatus().equals(Purchase.Status.OPEN)){
						// TODO СИНХРОНИЗИРОВАТЬ КОЛИЧЕСТВО ТОВАРА!
						user.getCart().getEntries().add(new CartEntry(
								null,
								user.getCart(),
								good,
								add.getType(),
								add.getQuantity()
						));

						good.setQuantity(good.getQuantity() - add.getQuantity());

						em.merge(user.getCart());
						em.merge(good);
						return Response.ok().build();
					} else {
						return Response.status(Response.Status.FORBIDDEN).entity("Purchase status is not OPEN").build();
					}
				} else {
					return Response.status(Response.Status.BAD_REQUEST).entity("Goods amount is not correct").build();
				}
			} else {
				return Response.status(Response.Status.BAD_REQUEST).entity("Goods amount is not correct").build();
			}
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response updateToCart(CartObjectUpdate upd) {

		if (session != null){

			SecurityUser user = session.getUser();

			CartEntry entry = user.getCart().getEntries().stream().filter(entr -> entr.getId() == upd.getEntryId())
					.collect(Collectors.toList()).get(0);

			Good updGood = em.find(Good.class, upd.getGoodId());
			Good good = entry.getGood();

			if (entry != null && updGood != null){

				if (updGood.getId() == good.getId()){

					good.setQuantity(good.getQuantity() + entry.getQuantity());
					entry.setQuantity(0f);

					if (good.getQuantity() >= upd.getQuantity()){
						if (good.getPurchase().getStatus().equals(Purchase.Status.OPEN)){
							entry.setQuantity(upd.getQuantity());
							good.setQuantity(good.getQuantity() - upd.getQuantity());

							em.merge(entry);
							em.merge(good);
						}
					} else {
						return Response.status(Response.Status.BAD_REQUEST).entity("Goods amount is not correct").build();
					}
				} else {
					if (updGood.getQuantity() >= upd.getQuantity()){
						good.setQuantity(good.getQuantity() + entry.getQuantity());
						entry.setQuantity(0f);

						entry.setGood(updGood);
						updGood.setQuantity(updGood.getQuantity() - upd.getQuantity());
						entry.setQuantity(upd.getQuantity());

						em.merge(entry);
						em.merge(good);
						em.merge(updGood);
					} else {
						return Response.status(Response.Status.BAD_REQUEST).entity("Goods amount is not correct").build();
					}
				}
			} else {
				return Response.status(Response.Status.BAD_REQUEST).entity("Not found good entry").build();
			}

			/*
			// TODO СИНХРОНИЗИРОВАТЬ КОЛИЧЕСТВО ТОВАРА!
			entry.setQuantity(upd.amount);
			//TODO add size, color, etc

			em.merge(entry.getCart());
			return Response.ok().build();
			*/
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@POST
	@Path("/makeorder")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
//	@RolesAllowed("user")
	public Response makeOrder(MakeOrder mk){

		if (session != null){
			
			List<OrderEntry> entries = new ArrayList<>();

			Order order = new Order();
			SecurityUser user = session.getUser();

			for (CartEntry entr : mk.getEntries()){
				if (user.getCart().getEntries().stream().filter(e -> e.getId() == entr.getId()).findFirst().isPresent()){
					entries.add(
							new OrderEntry(
							        null,
									order,
									em.find(Good.class, entr.getGood().getId()),
									entr.getQuantity(),
									false
							)
					);
				} else {
					return Response.status(Response.Status.FORBIDDEN).entity("Entry do not belong to current user").build();
				}
			}

			order.setUser(user);
			order.setSeller(entries.get(0).getGood().getPurchase().getUser());
			order.setStatus(Order.Status.NEW);
			order.setDPlace(em.find(Depot.class, em.find(Depot.class, mk.getDpId()).getId()));
			order.setEntries(entries);
			order.setDate(new Date());

			order.setResType(Aspect.EXTENDED);
			order.setAlias(String.format("/user/%s/order/", user.getId()));

			for (CartEntry entr : mk.getEntries()){
				em.remove(em.find(CartEntry.class, entr.getId()));
			}

			em.persist(order);
			em.flush();

			order.setAlias(String.format("/user/%s/order/%s", user.getId(), order.getId()));
			em.merge(order);

//				return Response.ok(new GenericEntity<OrderEntity>(order) {}).build();
			return Response.ok().build();
			
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@GET
	@Path("/deliveryplace/items")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response getDeliveryPlaceItems() {

		try {
			if (session != null){

				List<Depot> result = stores.findAllByResType(Aspect.EXTENDED);

				return Response.ok(new GenericEntity<List<Depot>>(result) {}).build();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (Exception e){
			throw e;
		}
	}

	@GET
	@Path("/items/{pid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response getCartItemsToOrder(
			@PathParam("pid") long pid
	) {

		if (session != null){
			Cart cart = session.getUser().getCart();

			List<CartEntry> entries = em.createNamedQuery("CartEntryEntity.accessiblePurchaseIdAndCartId", CartEntry.class)
					.setParameter("pid", pid)
					.setParameter("cid", cart.getId())
					.getResultList();

			for (CartEntry entr : entries){
				entr.getGood().getPurchase();
			}
			return Response.ok(new GenericEntity<List<CartEntry>>(entries) {}).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response delete(@PathParam("id") long id) {

	    if (session != null){

            CartEntry entry = session.getUser().getCart().getEntries().stream().filter(entr -> entr.getId() == id).findFirst().get();

            if (entry != null) {

                entry.getGood().setQuantity(entry.getGood().getQuantity() + entry.getQuantity());
                em.merge(entry.getGood());

                CartEntry rmEntry = em.find(CartEntry.class, entry.getId());

                em.remove(rmEntry);
                em.flush();
            }
            
        } else {
            
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok().build();
	}
	
	   @Data
	    @NoArgsConstructor
	    @AllArgsConstructor
	    public static class CartObject {
	        private long goodId;
	        private float quantity;
	        private CartEntry.Type type;
	    }

	//  @NoArgsConstructor
	//  @AllArgsConstructor
	//  public static class DeliveryPlaceSynthetic {
//	      private long Id;
//	      private float quantity;
//	      private CartEntryEntity.Type type;
	//  }

	    @Data
	    @NoArgsConstructor
	    @AllArgsConstructor
	    public static class CartObjectUpdate {
	        private long entryId;
	        private long goodId;
	        private float quantity;
	        private CartEntry.Type type;
	    }

	    @Data
	    @NoArgsConstructor
	    @AllArgsConstructor
	    public static class MakeOrder {
	        
	        private List<CartEntry> entries;
	        private long dpId;                                  //TODO переделать на Id
	    }
}
