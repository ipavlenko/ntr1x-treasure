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

import com.ntr1x.treasure.web.model.p2.Provider;
import com.ntr1x.treasure.web.services.IProviderService;
import com.ntr1x.treasure.web.services.IProviderService.ProviderCreate;
import com.ntr1x.treasure.web.services.IProviderService.ProviderUpdate;
import com.ntr1x.treasure.web.services.IProviderService.ProvidersResponse;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("Purchases")
@Component
@Path("providers")
@PermitAll
public class ProviderResource {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private IProviderService providers;
    
    @Inject
    private ISecurityService security;
    
    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Provider select(@PathParam("id") long id) {
        
        return providers.select(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public ProvidersResponse list(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size
    ) {
        
        return providers.list(page, size);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///providers:admin" })
    @Transactional
    public Provider create(ProviderCreate create) {
        
        Provider provider = providers.create(create);
        
        security.grant(provider.getUser(), provider.getAlias(), "admin");
        
        return provider;
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///providers/i/{id}:admin" })
    @Transactional
    public Provider update(@PathParam("id") long id, ProviderUpdate update) {
        
        Provider provider = providers.update(id, update);
        return provider;
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///providers/i/{id}:admin" })
    @Transactional
    public Provider remove(@PathParam("id") long id) {
        
        return providers.remove(id);
    }
}
