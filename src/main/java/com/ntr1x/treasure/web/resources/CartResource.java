package com.ntr1x.treasure.web.resources;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.security.PermitAll;
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

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.Cart;
import com.ntr1x.treasure.web.model.CartEntry;
import com.ntr1x.treasure.web.model.Depot;
import com.ntr1x.treasure.web.model.Grant;
import com.ntr1x.treasure.web.model.Modification;
import com.ntr1x.treasure.web.model.Order;
import com.ntr1x.treasure.web.model.OrderEntry;
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.CartEntryRepository;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Carts")
@Component
@Path("/carts")
@PermitAll
public class CartResource {

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private CartEntryRepository entries;
	
	@GET
	@Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "res:///carts/i/{id}:admin" })
    @Transactional
    public CartResponse select(@PathParam("id") long id) {
	    
	    Cart cart = em.find(Cart.class, id);
	    
        return new CartResponse(
            cart,
            cart.getEntries()
        );
    }
	
	@GET
    @Path("/i/{id}/entries")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///carts/i/{id}:admin" })
    @Transactional
    public List<CartEntry> select(@PathParam("id") long id, @QueryParam("purchase") Long purchase) {
        
        Cart cart = em.find(Cart.class, id);
        return purchase == null
            ? cart.getEntries()
            : entries.getByCartIdAndModificationGoodPurchaseId(cart.getId(), purchase)
        ;
    }
	
	@POST
	@Path("/i/{id}/entries")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "res:///carts/i/{id}:admin" })
	@Transactional
	public CartEntry create(@PathParam("id") long id, CreateEntry entry) {
		
	    if (entry.quantity <= 0) {
	        throw new WebApplicationException("Negative quantity", Response.Status.BAD_REQUEST);
	    }
	    
	    Cart cart = em.find(Cart.class, id);
		Modification modification = em.find(Modification.class, entry.modification);

		if (modification.getQuantity() < entry.quantity) {
		    throw new WebApplicationException( "Goods amount is not correct", Response.Status.CONFLICT);
		}
		
		if (modification.getGood().getPurchase().getStatus().equals(Purchase.Status.OPEN)) {
            throw new WebApplicationException( "Purchase is not OPEN", Response.Status.CONFLICT);
		}
					    
		CartEntry e = new CartEntry(); {
            e.setCart(cart);
            e.setModification(modification);
            e.setType(entry.type);
            e.setQuantity(entry.quantity);
		}

		modification.setQuantity(modification.getQuantity() - entry.quantity);

		em.persist(e);
		em.flush();
		
		e.setAlias(ResourceUtils.alias(cart, "entries/i", e));
		
		em.merge(e);
		em.merge(modification);
		
		em.flush();
		
		return e;
	}

	@PUT
	@Path("/i/{id}/entries/i/{eid}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///carts/i/{id}:admin" })
	@Transactional
	public CartEntry update(@PathParam("id") long id, @PathParam("eid") long eid, UpdateEntry entry) {
		
	    Cart cart = em.find(Cart.class, id);
		CartEntry e = em.find(CartEntry.class, eid);
		
		if (e.getCart().getId() != cart.getId()) {
		    throw new WebApplicationException("Is not in the cart", Response.Status.CONFLICT);
		}
		
		Modification modification = e.getModification();
		if (modification.getId() != entry.modification) {
		    
		    modification.setQuantity(modification.getQuantity() + e.getQuantity());
		    
		    em.merge(modification);
		    
            modification = em.find(Modification.class, entry.modification);
		}
		
		if (modification.getQuantity() < entry.quantity) {
		    throw new WebApplicationException( "Goods amount is not correct", Response.Status.CONFLICT);
		}
		
		modification.setQuantity(modification.getQuantity() - entry.quantity);
		
		e.setModification(modification);
		e.setQuantity(entry.quantity);
		
		em.merge(modification);
		em.merge(e);
		
		em.flush();
	
		return e;
	}
	
	@DELETE
    @Path("/i/{id}/entries/i/{eid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///carts/i/{id}:admin" })
    @Transactional
    public CartEntry remove(@PathParam("id") long id, @PathParam("eid") long eid) {
	    
	    Cart cart = em.find(Cart.class, id);
        CartEntry e = em.find(CartEntry.class, eid);
        
        if (e.getCart().getId() != cart.getId()) {
            throw new WebApplicationException("Is not in the cart", Response.Status.CONFLICT);
        }
        
        Modification modification = e.getModification();
        modification.setQuantity(modification.getQuantity() + e.getQuantity());
        
        em.merge(modification);
        em.remove(e);
        
        em.flush();
        
        return e;
	}
    

	@POST
	@Path("/i/{id}/orders")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///carts/i/{id}:admin" })
	public Order order(@PathParam("id") long id, OrderRequest r) {

	    Purchase p = em.find(Purchase.class, r.purchase);
	    Depot d = em.find(Depot.class, r.depot);
	    Cart cart = em.find(Cart.class, id);
	    User user = cart.getUser();
	    
	    Order order = new Order(); {
	        order.setStatus(Order.Status.NEW);
	        order.setPurchase(p);
	        order.setDepot(d);
	        order.setCreated(LocalDateTime.now());
	        order.setUser(user);
	    }
	    
	    em.persist(order);
	    em.flush();
	    
	    order.setAlias(ResourceUtils.alias(null, "orders/i", order));
	    
	    em.merge(order);
	    em.flush();
	    
	    Grant grant = new Grant(); {
	        grant.setAccount(user);
	        grant.setAction("admin");
	        grant.setAlias(order.getAlias());
	    }
	    
	    em.persist(grant);
        em.flush();
	    
        grant.setAlias(ResourceUtils.alias(user, "grants/i", grant));
        
        em.merge(grant);
        em.flush();
        
	    for (CartEntry entry : entries.getByCartIdAndModificationGoodPurchaseId(cart.getId(), p.getId())) {
	        
	        OrderEntry oe = new OrderEntry(); {
	            
	            oe.setOrder(order);
	            oe.setQuantity(entry.getQuantity());
	            oe.setConfirmed(false);
	            
	            em.persist(oe);
	            em.flush();
	            
	            oe.setAlias(ResourceUtils.alias(order, "entries/i", oe));
	            
	            em.merge(oe);
	            em.flush();
	        }
	    }
	    
	    em.refresh(order);
	    
	    return order;
	}
	
	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public class OrderRequest {
        
        public long purchase;
        public long depot;
    }
	
	@XmlRootElement
	@NoArgsConstructor
	@AllArgsConstructor
	public class CartResponse {
	    
	    public Cart cart;
	    public List<CartEntry> entries;
	}
	
	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateEntry {
	    
        public long modification;
        public float quantity;
        public CartEntry.Type type;
    }

	//  @NoArgsConstructor
	//  @AllArgsConstructor
	//  public static class DeliveryPlaceSynthetic {
//	      private long Id;
//	      private float quantity;
//	      private CartEntryEntity.Type type;
	//  }

    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateEntry {
        
        public long modification;
        public float quantity;
        public Action action;
    }

//	    @Data
//	    @NoArgsConstructor
//	    @AllArgsConstructor
//	    public static class MakeOrder {
//	        
//	        private List<CartEntry> entries;
//	        private long dpId;                                  //TODO переделать на Id
//	    }
}
