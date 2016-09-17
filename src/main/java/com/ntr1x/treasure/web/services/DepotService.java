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
import com.ntr1x.treasure.web.model.p2.Depot;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.DepotRepository;

@Service
public class DepotService implements IDepotService {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private DepotRepository depots;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private IAttributeService attributes;
    
    @Override
    public Depot create(DepotCreate create) {
        
        Depot depot = new Depot(); {
    
            User user = em.find(User.class, create.user);
            
            depot.setTitle(create.title);
            depot.setDeliveryPrice(create.deliveryPrice);
            depot.setUser(user);
            
            em.persist(depot);
            em.flush();
        }
        
        security.register(depot, ResourceUtils.alias(null, "depots/i", depot));
        
        attributes.createAttributes(depot, create.attributes);
        
        return depot;
    }

    @Override
    public Depot update(long id, DepotUpdate update) {
        
        Depot depot = em.find(Depot.class, id); {
            
            depot.setTitle(update.title);
            depot.setDeliveryPrice(update.deliveryPrice);
            
            em.merge(depot);
            em.flush();
        }
        
        attributes.updateAttributes(depot, update.attributes);
        
        return depot;
    }

    @Override
    public Depot remove(long id) {
        
        Depot depot = em.find(Depot.class, id);

        if (!depot.getOrders().isEmpty()) {
            throw new WebApplicationException("Depot in use", Response.Status.CONFLICT);
        }

        em.remove(depot);
        em.flush();
        
        return depot;
    }

    @Override   
    public DepotsResponse list(int page, int size) {
        
        Page<Depot> p = depots.findAll(new PageRequest(page, size));
        
        return new DepotsResponse(
            p.getTotalElements(),
            page,
            size,
            p.getContent()
        );
    }

    @Override
    public Depot select(long id) {
        
        Depot depot = em.find(Depot.class, id);
        return depot;
    }
    
}
