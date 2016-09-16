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

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.p1.Attribute;
import com.ntr1x.treasure.web.services.IAttributeService;
import com.ntr1x.treasure.web.services.IAttributeService.AttributeCreate;
import com.ntr1x.treasure.web.services.IAttributeService.AttributeUpdate;
import com.ntr1x.treasure.web.services.IAttributeService.AttributesResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("Resources")
@Component
@Path("attributes")
@PermitAll
public class AttributeResource {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private IAttributeService attributes;
    
    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Attribute select(@PathParam("id") long id) {
        
        return attributes.select(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public AttributesResponse list(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size,
        @QueryParam("aspect") List<String> aspect
    ) {
        
        return attributes.list(page, size, aspect);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///attributes:admin" })
    @Transactional
    public Attribute create(AttributeCreate create) {
        
        Attribute attribute = attributes.create(create);
        return attribute;
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///attributes/i/{id}:admin" })
    @Transactional
    public Attribute update(@PathParam("id") long id, AttributeUpdate update) {
        
        Attribute attribute = attributes.update(id, update);
        return attribute;
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///attributes/i/{id}:admin" })
    @Transactional
    public Attribute remove(@PathParam("id") long id) {
        
        return attributes.remove(id);
    }
}
