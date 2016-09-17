package com.ntr1x.treasure.web.resources;

import java.util.Collections;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.index.ModificationIndexRepository;
import com.ntr1x.treasure.web.index.ModificationIndexRepositoryCustom.SearchRequest;
import com.ntr1x.treasure.web.index.ModificationIndexRepositoryCustom.SearchRequest.GroupBy;
import com.ntr1x.treasure.web.index.ModificationIndexRepositoryCustom.SearchResult;
import com.ntr1x.treasure.web.model.p2.Purchase;
import com.ntr1x.treasure.web.model.p2.Session;
import com.ntr1x.treasure.web.model.p4.Modification;
import com.ntr1x.treasure.web.repository.ModificationRepository;
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
    private ModificationRepository repository;
    
    @Inject
    private IModificationService modifications;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private Session session;
    
    @Inject
    private ModificationIndexRepository search;

    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Modification select(@PathParam("id") long id) {
        
        return modifications.select(id);
    }
    
    @GET
    @Path("/grouped")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public ModificationsResponse modificationsQuery(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size,
        @QueryParam("groupBy") @ApiParam(example = "PURCHASE") @DefaultValue("PURCHASE") GroupBy groupBy,
        @QueryParam("query") String query,
        @QueryParam("purchase") Long purchase,
        @QueryParam("categories") List<Long> categories,
        @QueryParam("attributes") List<String> attributes,
        @QueryParam("good") Long good,
        @QueryParam("user") Long user,
        @QueryParam("status") Purchase.Status status
    ) {
        
        SearchResult result =
            search.search(
                new SearchRequest(
                    query,
                    purchase,
                    good,
                    categories,
                    attributes,
                    page,
                    size,
                    groupBy
                 )
            )
        ;
        
        Long[] identifiers = result.items.toArray(new Long[0]);

        List<Modification> list = identifiers.length > 0
            ? repository.findByIdIn(identifiers)
            : Collections.emptyList()
        ;
        
        return new ModificationsResponse(
            result.count,
            page,
            size,
            list
        );
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
