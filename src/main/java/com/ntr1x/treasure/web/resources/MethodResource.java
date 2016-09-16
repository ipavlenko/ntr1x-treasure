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

import com.ntr1x.treasure.web.model.p2.Method;
import com.ntr1x.treasure.web.services.IMethodService;
import com.ntr1x.treasure.web.services.ISecurityService;
import com.ntr1x.treasure.web.services.IMethodService.MethodCreate;
import com.ntr1x.treasure.web.services.IMethodService.MethodUpdate;
import com.ntr1x.treasure.web.services.IMethodService.MethodsResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("Purchases")
@Component
@Path("methods")
@PermitAll
public class MethodResource {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private IMethodService methods;
    
    @Inject
    private ISecurityService security;
    
    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Method select(@PathParam("id") long id) {
        
        return methods.select(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public MethodsResponse list(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size
    ) {
        
        return methods.list(page, size);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///methods:admin" })
    @Transactional
    public Method create(MethodCreate create) {
        
        Method method = methods.create(create);
        
        security.grant(method.getUser(), method.getAlias(), "admin");
        
        return method;
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///methods/i/{id}:admin" })
    @Transactional
    public Method update(@PathParam("id") long id, MethodUpdate update) {
        
        Method method = methods.update(id, update);
        return method;
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///methods/i/{id}:admin" })
    @Transactional
    public Method remove(@PathParam("id") long id) {
        
        return methods.remove(id);
    }
}
