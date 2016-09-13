package com.ntr1x.treasure.web.resources;

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
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Good;
import com.ntr1x.treasure.web.model.Modification;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.ModificationRepository;
import com.ntr1x.treasure.web.services.IAttributeService;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Modifications")
@Component
@Path("modifidactions")
@PermitAll
public class ModificationResousce {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ModificationRepository modifications;
    
    @Inject
    private IAttributeService params;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private Session session;

    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Modification select(@PathParam("id") long id) {
        
        Modification modification = em.find(Modification.class, id);
        return modification;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public ModificationsResponse list(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size
    ) {
        
        Page<Modification> p = modifications.findAll(new PageRequest(page, size));
        
        return new ModificationsResponse(
            p.getTotalElements(),
            page,
            size,
            p.getContent()
        );
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///modifications:admin" })
    @Transactional
    public Modification create(ModificationCreate create) {
        
        Modification modification = new Modification(); {
        
            Good good = em.find(Good.class, create.good);
            
            modification.setGood(good);
            modification.setPrice(create.price);
            modification.setQuantity(create.quantity);
            modification.setSizeRange(create.sizeRange);
            
            em.persist(modification);
            em.flush();
        }
        
        security.register(modification, ResourceUtils.alias(null, "modifications/i", modification));
        security.grant(session.getUser(), modification.getAlias(), "admin");
        
        params.createAttributes(modification, create.params);
        
        return modification;
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///modifications/i/{id}:admin" })
    @Transactional
    public Modification update(@PathParam("id") long id, ModificationUpdate update) {
        
        Modification modification = em.find(Modification.class, id); {
            
            modification.setPrice(update.price);
            modification.setQuantity(update.quantity);
            modification.setSizeRange(update.sizeRange);
            
            em.merge(modification);
            em.flush();
        }
        
        params.updateAttributes(modification, update.params);
        
        return modification;
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///modification/i/{id}:admin" })
    @Transactional
    public Modification remove(@PathParam("id") long id) {
        
        Modification modification = em.find(Modification.class, id);

        em.remove(modification);
        em.flush();
        
        return modification;
    }

    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModificationCreate {
        
        public long good;
        public float price;
        public float quantity;
        public float sizeRange;
        
        @XmlElement
        public IAttributeService.CreateAttribute[] params;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModificationUpdate {
        
        public float price;
        public float quantity;
        public float sizeRange;
        
        @XmlElement
        public IAttributeService.UpdateAttribute[] params;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModificationsResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Modification> modifications;
    }
}

