package com.ntr1x.treasure.web.resources;

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

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.p2.Depot;
import com.ntr1x.treasure.web.services.IDepotService;
import com.ntr1x.treasure.web.services.IDepotService.DepotCreate;
import com.ntr1x.treasure.web.services.IDepotService.DepotUpdate;
import com.ntr1x.treasure.web.services.IDepotService.DepotsResponse;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("Depots")
@Component
@Path("depots")
@PermitAll
public class DepotResource {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private IDepotService depots;
    
    @Inject
    private ISecurityService security;
    
    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Depot select(@PathParam("id") long id) {
        
        return depots.select(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public DepotsResponse list(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size
    ) {
        
        return depots.list(page, size);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///depots:admin" })
    @Transactional
    public Depot create(DepotCreate create) {
        
        Depot depot = depots.create(create);
        
        security.grant(depot.getUser(), depot.getAlias(), "admin");
        
        return depot;
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///depots/i/{id}:admin" })
    @Transactional
    public Depot update(@PathParam("id") long id, DepotUpdate update) {
        
        Depot depot = depots.update(id, update);
        return depot;
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///depots/i/{id}:admin" })
    @Transactional
    public Depot remove(@PathParam("id") long id) {
        
        return depots.remove(id);
    }
}
