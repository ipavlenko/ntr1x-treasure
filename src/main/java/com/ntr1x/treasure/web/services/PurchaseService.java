package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.Method;
import com.ntr1x.treasure.web.model.Provider;
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.reflection.ResourceUtils;

@Service
public class PurchaseService implements IPurchaseService {

    @PersistenceContext
    private EntityManager em;
    
//    @Inject
//    private PurchaseRepository purchases;
    
    @Inject
    private IImageService imageService;
    
    @Inject
    private IAttributeService attributeService;
    
    @Inject
    private ISecurityService security;
    
    @Override
    public Purchase create(PurchaseCreate create) {
        
        Purchase purchase = new Purchase(); {
            
            Method method = em.find(Method.class, create.method);
            Provider provider = em.find(Provider.class, create.provider);
            User user = em.find(User.class, create.user);
            
            purchase.setTitle(create.title);
            purchase.setStatus(Purchase.Status.NEW);
            purchase.setMethod(method);
            purchase.setProvider(provider);
            purchase.setUser(user);
            purchase.setOpen(create.open);
            purchase.setStop(create.stop);
            purchase.setDelivery(create.delivery);
            purchase.setNextDelivery(create.nextDelivery);
            
            em.persist(purchase);
            em.flush();
        }
        
        security.register(purchase, ResourceUtils.alias(null, "purchases/i", purchase));
        
        attributeService.createAttributes(purchase, create.attributes);
        imageService.createImages(purchase, create.images);
        
        return purchase;
    }

    @Override
    public Purchase update(long id, PurchaseUpdate update) {
        
        Purchase purchase = em.find(Purchase.class, id); {
            
            Method method = em.find(Method.class, update.method);
            Provider provider = em.find(Provider.class, update.provider);
            User user = em.find(User.class, update.user);
            
            purchase.setTitle(update.title);
            purchase.setMethod(method);
            purchase.setProvider(provider);
            purchase.setUser(user);
            purchase.setOpen(update.open);
            purchase.setStop(update.stop);
            purchase.setDelivery(update.delivery);
            purchase.setNextDelivery(update.nextDelivery);
            
            em.merge(purchase);
            em.flush();
        }
        
        attributeService.updateAttributes(purchase, update.attributes);
        imageService.updateImages(purchase, update.images);
        
        return purchase;
    }
    
    
}