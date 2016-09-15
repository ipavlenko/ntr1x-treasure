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
import com.ntr1x.treasure.web.model.p2.Method;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.MethodRepository;

@Service
public class MethodService implements IMethodService {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private MethodRepository methods;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private IAttributeService attributes;
    
    
    
    @Override
    public Method create(MethodCreate create) {
        
        Method method = new Method(); {
    
            User user = em.find(User.class, create.user);
            
            method.setTitle(create.title);
            method.setUser(user);
            
            em.persist(method);
            em.flush();
        }
        
        security.register(method, ResourceUtils.alias(null, "methods/i", method));
        
        attributes.createAttributes(method, create.attributes);
        
        return method;
    }

    @Override
    public Method update(long id, MethodUpdate update) {
        
        Method method = em.find(Method.class, id); {
            
            User user = em.find(User.class, update.user);
            
            method.setTitle(update.title);
            method.setUser(user);
            
            em.merge(method);
            em.flush();
        }
        
        attributes.updateAttributes(method, update.attributes);
        
        return method;
    }

    @Override
    public Method remove(long id) {
        
        Method method = em.find(Method.class, id);

        if (!method.getPurchases().isEmpty()) {
            throw new WebApplicationException("Method in use", Response.Status.CONFLICT);
        }

        em.remove(method);
        em.flush();
        
        return method;
    }

    @Override   
    public MethodsResponse list(int page, int size) {
        
        Page<Method> p = methods.findAll(new PageRequest(page, size));
        
        return new MethodsResponse(
            p.getTotalElements(),
            page,
            size,
            p.getContent()
        );
    }

    @Override
    public Method select(long id) {
        
        Method method = em.find(Method.class, id);
        return method;
    }
    
}
