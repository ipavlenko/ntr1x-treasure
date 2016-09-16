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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.p2.Session;
import com.ntr1x.treasure.web.model.p4.Modification;
import com.ntr1x.treasure.web.services.IModificationService;
import com.ntr1x.treasure.web.services.IModificationService.ModificationCreate;
import com.ntr1x.treasure.web.services.IModificationService.ModificationUpdate;
import com.ntr1x.treasure.web.services.IModificationService.ModificationsResponse;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("Purchases")
@Component
@Path("modifications")
public class ModificationResource {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private IModificationService modifications;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private Session session;

    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Modification select(@PathParam("id") long id) {
        
        return modifications.select(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public ModificationsResponse list(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size
    ) {
        return modifications.list(page, size);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///modifications:admin" })
    @Transactional
    public Modification create(ModificationCreate create) {
        
        Modification modification = modifications.create(create);
        
        security.grant(session.getUser(), modification.getAlias(), "admin");
        
        return modification;
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///modifications/i/{id}:admin" })
    @Transactional
    public Modification update(@PathParam("id") long id, ModificationUpdate update) {
        
        return modifications.update(id, update);
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///modifications/i/{id}:admin" })
    @Transactional
    public Modification remove(@PathParam("id") long id) {
        
        return modifications.remove(id);
    }
}
