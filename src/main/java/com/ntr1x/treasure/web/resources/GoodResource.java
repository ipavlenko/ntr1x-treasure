package com.ntr1x.treasure.web.resources;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.p2.Purchase;
import com.ntr1x.treasure.web.model.p3.Good;
import com.ntr1x.treasure.web.services.IGoodService;
import com.ntr1x.treasure.web.services.IGoodService.CreateRequest;
import com.ntr1x.treasure.web.services.IGoodService.UpdateRequest;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;

@Api("Goods")
@Component
@Path("goods")
public class GoodResource {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ISecurityService security;
    
	@Inject
    private IGoodService goods;
	
//	@Inject
//  private Session session;

	@GET
	@Path("/i/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Good select(@PathParam("id") long id) {
	    
	    Good good = em.find(Good.class, id);
        return good;
	}
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///goods:admin" })
    public Good create(CreateRequest create) {
	    
	    Good good = goods.create(create);
        
        security.grant(good.getPurchase().getUser(), good.getAlias(), "admin");
        
        return good;
    }
	
	@POST
	@Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///goods/i/{id}:admin" })
    public Good update(@PathParam("id") long id, UpdateRequest update) {
	    
	    return goods.update(id, update);
    }
	
	@DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Good remove(@PathParam("id") long id) {

	    Good good = em.find(Good.class, id);
	    
	    Purchase p = good.getPurchase();
	    switch (p.getStatus()) {
    	    case NEW:
    	    case MODERATION:
    	        break;
            default:
                throw new WebApplicationException("Purchase state doesn't allow modification", Response.Status.CONFLICT);
	    }
	    
	    em.remove(good);
        em.flush();
        
        return good;
    }
	
}
