package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.events.ResourceEvent;
import com.ntr1x.treasure.web.model.p2.Purchase;
import com.ntr1x.treasure.web.model.p3.Good;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.services.ISubscriptionService.ResourceMessage;

@Service
public class GoodService implements IGoodService {

    @Inject
    private EntityManager em;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private IPublisherSevice publisher;
    
    @Inject
    private ITransactionService transactions;
    
    @Inject
    private IAttributeService attributes;
    
    @Inject
    private IModificationService modifications;
    
    @Inject
    private ICategoryService categories;
    
    @Inject
    private IGoodService goods;
    
    @Override
    public Good create(CreateRequest request) {
        
        Good g = new Good(); {
            
            Purchase p = em.find(Purchase.class, request.purchase);
            
            switch (p.getStatus()) {
                case NEW:
                case MODERATION:
                case OPEN:
                    break;
                default:
                    throw new WebApplicationException("Current state of the purchase doesn't allow modification", Response.Status.CONFLICT);
            }
            
            g.setPurchase(p);
            g.setTitle(request.title);
            g.setPromo(request.promo);
            
            em.persist(g);
            em.flush();
        }
        
        security.register(g, ResourceUtils.alias(null, "goods/i", g));
        
        attributes.createAttributes(g, request.attributes);
        categories.createCategories(g, request.categories);
        modifications.createModifications(g, request.modifications);
        
        em.refresh(g);
        
        transactions.afterCommit(() -> {
            
            publisher.publishEvent(
                new ResourceEvent(
                    new ResourceMessage(g.getAlias(), ResourceMessage.Type.CREATE, g)
                )
            );
        });
        
        return g;
    }

    @Override
    public Good update(long id, UpdateRequest request) {
        
        Good g = em.find(Good.class, id); {
            
            Purchase p = g.getPurchase();
            
            switch (p.getStatus()) {
                case NEW:
                case MODERATION:
                case OPEN:
                    break;
                default:
                    throw new WebApplicationException("Current state of the purchase doesn't allow modification", Response.Status.CONFLICT);
            }
            
            g.setTitle(request.title);
            g.setPromo(request.promo);
            
            em.merge(g);
            em.flush();
            
            attributes.updateAttributes(g, request.attributes);
            categories.updateCategories(g, request.categories);
            modifications.updateModifications(g, request.modifications);
        }
        
        em.refresh(g);
        
        transactions.afterCommit(() -> {
            
            publisher.publishEvent(
                new ResourceEvent(
                    new ResourceMessage(g.getAlias(), ResourceMessage.Type.UPDATE, g)
                )
            );
        });
        
        return g;
    }
    
    @Override
    public Good remove(long id) {
        
        Good g = em.find(Good.class, id); {
        
            Purchase p = g.getPurchase();
            switch (p.getStatus()) {
                case NEW:
                case MODERATION:
                    break;
                default:
                    throw new WebApplicationException("Purchase state doesn't allow modification", Response.Status.CONFLICT);
            }
            
            em.remove(g);
            em.flush();
        }
        
        em.refresh(g);
        
        transactions.afterCommit(() -> {
            
            publisher.publishEvent(
                new ResourceEvent(
                    new ResourceMessage(g.getAlias(), ResourceMessage.Type.REMOVE, g)
                )
            );
        });
        
        return g;
    }
    
    @Override
    public Good select(long id) {
        
        Good good = em.find(Good.class, id);
        return good;
    }
    
    @Override
    public GoodsResponse list(int page, int size, Long purchase) {
        
        return goods.list(page, size, purchase);
    }
}
