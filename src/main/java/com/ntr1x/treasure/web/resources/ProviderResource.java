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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Provider;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.ProviderRepository;
import com.ntr1x.treasure.web.services.IParamService;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
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
    private IParamService params;
    
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
    @Transactional
    public Provider list() {
        
        Provider provider = em.find(Provider.class, id);
        return provider;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "res:///providers:admin" })
    @Transactional
    public Provider create(@PathParam("id") long id, ProviderCreate create) {
        
        Provider provider = new Provider(); {
            
            provider.setTitle(create.title);
            provider.setPromo(create.promo);
            provider.setDescription(create.description);
            
            em.persist(provider);
            em.flush();
        }
        
        security.register(provider, ResourceUtils.alias(null, "provider/i", provider));
        security.grant(session.getUser(), provider.getAlias(), "admin");
        
        params.createParams(provider, create.params);
        
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
        
        params.updateParams(provider, update.params);
        
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
        public IParamService.CreateParam[] params;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviderUpdate {
        
        public String title;
        public String promo;
        public String description;
        
        @XmlElement
        public IParamService.UpdateParam[] params;
    }
}
