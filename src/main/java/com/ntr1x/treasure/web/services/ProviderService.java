package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.model.p2.Provider;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.ProviderRepository;

@Service
public class ProviderService implements IProviderService {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ProviderRepository providers;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private IAttributeService attributes;
    
    
    
    @Override
    public Provider create(ProviderCreate create) {
        
        Provider provider = new Provider(); {
    
            User user = em.find(User.class, create.user);
            
            provider.setTitle(create.title);
            provider.setPromo(create.promo);
            provider.setDescription(create.description);
            provider.setUser(user);
            
            em.persist(provider);
            em.flush();
        }
        
        security.register(provider, ResourceUtils.alias(null, "providers/i", provider));
        
        attributes.createAttributes(provider, create.attributes);
        
        return provider;
    }

    @Override
    public Provider update(long id, ProviderUpdate update) {
        
        Provider provider = em.find(Provider.class, id); {
            
            User user = em.find(User.class, update.user);
            
            provider.setTitle(update.title);
            provider.setPromo(update.promo);
            provider.setDescription(update.description);
            provider.setUser(user);
            
            em.merge(provider);
            em.flush();
        }
        
        attributes.updateAttributes(provider, update.attributes);
        
        return provider;
    }

    @Override
    public Provider remove(long id) {
        
        Provider provider = em.find(Provider.class, id);

        if (!provider.getPurchases().isEmpty()) {
            throw new WebApplicationException("Provider in use", Response.Status.CONFLICT);
        }

        em.remove(provider);
        em.flush();
        
        return provider;
    }

    @Override   
    public ProvidersResponse list(int page, int size) {
        
        Page<Provider> p = providers.findAll(new PageRequest(page, size));
        
        return new ProvidersResponse(
            p.getTotalElements(),
            page,
            size,
            p.getContent()
        );
    }

    @Override
    public Provider select(long id) {
        
        Provider provider = em.find(Provider.class, id);
        return provider;
    }
    
}
