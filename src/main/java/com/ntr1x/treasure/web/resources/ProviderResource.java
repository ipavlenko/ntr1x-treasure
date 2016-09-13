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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Provider;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.ProviderRepository;
import com.ntr1x.treasure.web.services.IAttributeService;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Providers")
@Component
@Path("providers")
@PermitAll
public class ProviderResource {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ProviderRepository providers;
    
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
    public Provider select(@PathParam("id") long id) {
        
        Provider provider = em.find(Provider.class, id);
        return provider;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public ProvidersResponse list(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size
    ) {
        
        Page<Provider> p = providers.findAll(new PageRequest(page, size));
        
        return new ProvidersResponse(
            p.getTotalElements(),
            page,
            size,
            p.getContent()
        );
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///providers:admin" })
    @Transactional
    public Provider create(ProviderCreate create) {
        
        Provider provider = new Provider(); {
            
            provider.setTitle(create.title);
            provider.setPromo(create.promo);
            provider.setDescription(create.description);
            provider.setUser(session.getUser());
            
            em.persist(provider);
            em.flush();
        }
        
        security.register(provider, ResourceUtils.alias(null, "providers/i", provider));
        security.grant(session.getUser(), provider.getAlias(), "admin");
        
        params.createAttributes(provider, create.params);
        
        return provider;
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///providers/i/{id}:admin" })
    @Transactional
    public Provider update(@PathParam("id") long id, ProviderUpdate update) {
        
        Provider provider = em.find(Provider.class, id); {
            
            provider.setTitle(update.title);
            provider.setPromo(update.promo);
            provider.setDescription(update.description);
            
            em.merge(provider);
            em.flush();
        }
        
        params.updateAttributes(provider, update.params);
        
        return provider;
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///providers/i/{id}:admin" })
    @Transactional
    public Provider remove(@PathParam("id") long id) {
        
        Provider provider = em.find(Provider.class, id);

        if (!provider.getPurchases().isEmpty()) {
            throw new WebApplicationException("Provider in use", Response.Status.CONFLICT);
        }

        em.remove(provider);
        em.flush();
        
        return provider;
    }

    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviderCreate {
        
        public String title;
        public String promo;
        public String description;
        
        @XmlElement
        public IAttributeService.CreateAttribute[] params;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviderUpdate {
        
        public String title;
        public String promo;
        public String description;
        
        @XmlElement
        public IAttributeService.UpdateAttribute[] params;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvidersResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Provider> providers;
    }
}
