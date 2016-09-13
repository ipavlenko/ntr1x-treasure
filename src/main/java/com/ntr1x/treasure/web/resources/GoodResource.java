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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.Good;
import com.ntr1x.treasure.web.model.Modification;
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.GoodRepository;
import com.ntr1x.treasure.web.services.ICategoryService;
import com.ntr1x.treasure.web.services.IParamService;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Goods")
@Component
@Path("goods")
public class GoodResource {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private IParamService params;
    
	@Inject
    private ICategoryService categories;
	
	@Inject
    private GoodRepository goods;
	
	@Inject
    private Session session;

	@GET
	@Path("/i/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Good select(@PathParam("id") long id) {
	    
	    Good good = em.find(Good.class, id);
        return good;
	}
	
	@POST
	@Path("/i/{id}/goods")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///goods/i/{id}:admin" })
    public Good create(@PathParam("id") long id, CreateRequest request) {

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
	    }
	    
	    security.register(g, ResourceUtils.alias(null, "goods/i", g));
        
        params.createParams(g, request.params);
        categories.createCategories(g, request.categories);
        
        security.grant(session.getUser(), g.getAlias(), "admin");
	    
	    if (request.modifications != null) {
	        
	        for (CreateRequest.Modification modification : request.modifications) {
	            
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
    public Good update(@PathParam("id") long id, @PathParam("good") long good, UpdateRequest request) {

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
            
            for (UpdateRequest.Modification modification : request.modifications) {
                
                Modification m = new Modification(); {
                    
                    m.setGood(g);
                    m.setPrice(modification.price);
                    m.setSizeRange(modification.sizeRange);
                    
                    em.persist(m);
                    em.flush();
                    
                    security.register(m, ResourceUtils.alias(g, "modifications/i", g));
                    
                    params.updateParams(m, modification.params);
                }
            }
        }
        
        return g;
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

	@XmlRootElement
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateRequest {
	    
        public String title;
        public String promo;
        
        @XmlElement
        public IParamService.CreateParam[] params;
        
        @XmlElement
        public ICategoryService.CreateCategory[] categories;
        
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
    public static class UpdateRequest {
        
        public String title;
        public String promo;
        
        @XmlElement
        public IParamService.UpdateParam[] params;
        
        @XmlElement
        public ICategoryService.CreateCategory[] categories;
        
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
}
