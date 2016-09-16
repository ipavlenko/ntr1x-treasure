package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.ntr1x.treasure.web.events.ResourceEvent;
import com.ntr1x.treasure.web.model.p2.Purchase;
import com.ntr1x.treasure.web.model.p3.Good;
import com.ntr1x.treasure.web.model.p4.Modification;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.ModificationRepository;
import com.ntr1x.treasure.web.services.ISubscriptionService.ResourceMessage;

@Service
public class ModificationService implements IModificationService {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private IPublisherSevice publisher;
    
    @Inject
    private IAttributeService attributes;
    
    @Inject
    private ModificationRepository modifications;
    
//    @Inject
//    private ModificationIndexRepository index;
    
    @Override
    public Modification create(ModificationCreate create) {
        
        Modification modification = new Modification(); {
            
            Good good = em.find(Good.class, create.good);
            
            modification.setGood(good);
            modification.setPrice(create.price);
            modification.setQuantity(create.quantity);
            modification.setSizeRange(create.sizeRange);
            
            em.persist(modification);
            em.flush();
        }
        
        security.register(modification, ResourceUtils.alias(null, "modifications/i", modification));
        
        attributes.createAttributes(modification, create.attributes);
        
        em.flush();
        
        em.refresh(modification);
        
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
      
            @Override
            public void afterCommit() {
          
                publisher.publishEvent(
                    new ResourceEvent(
                        new ResourceMessage(
                            modification.getAlias(),
                            ResourceMessage.Type.CREATE,
                            modification
                        )
                    )
                );
            }
        });
        
        return modification;
    }
    
    @Override
    public Modification update(long id, ModificationUpdate update) {
        
        Modification modification = em.find(Modification.class, id); {
            
            modification.setPrice(update.price);
            modification.setQuantity(update.quantity);
            modification.setSizeRange(update.sizeRange);
            
            em.merge(modification);
            em.flush();
        }
        
        attributes.updateAttributes(modification, update.attributes);
        
        em.flush();
        
        em.refresh(modification);
        
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            
            @Override
            public void afterCommit() {
          
                publisher.publishEvent(
                    new ResourceEvent(
                        new ResourceMessage(
                            modification.getAlias(),
                            ResourceMessage.Type.UPDATE,
                            modification
                        )
                    )
                );
            }
        });
        
        return modification;
    }
    
    @Override
    public Modification remove(long id) {
        
        Modification modification = em.find(Modification.class, id); {
            
            Purchase p = modification.getGood().getPurchase();
            switch (p.getStatus()) {
                case NEW:
                case MODERATION:
                    break;
                default:
                    throw new WebApplicationException("Purchase state doesn't allow modification", Response.Status.CONFLICT);
            }
            
            em.remove(modification);
            em.flush();
        }
        
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            
            @Override
            public void afterCommit() {
          
                publisher.publishEvent(
                    new ResourceEvent(
                        new ResourceMessage(
                            modification.getAlias(),
                            ResourceMessage.Type.REMOVE,
                            modification
                        )
                    )
                );
            }
        });
        
        return modification;
    }
    
    @Override
    public Modification select(long id) {
        
        return em.find(Modification.class, id);
    }
    
    @Override
    public ModificationsResponse list(int page, int size) {
        
        Page<Modification> p = modifications.findAll(new PageRequest(page, size));
        
        return new ModificationsResponse(
            p.getTotalElements(),
            page,
            size,
            p.getContent()
        );
    }
    
    @Override
    public void createModifications(Good good, RelatedModification[] modifications) {
        
        if (modifications != null) {
            
            for (RelatedModification p : modifications) {
                
                Modification v = new Modification(); {
                    
                    v.setGood(good);
                    v.setPrice(p.price);
                    v.setSizeRange(p.sizeRange);
                    
                    em.persist(v);
                    em.flush();
                    
                    security.register(v, ResourceUtils.alias(null, "modifications/i", v));
                    
                    attributes.createAttributes(v, p.attributes);
                }
            }
            
            em.flush();
        }
    }

    @Override
    public void updateModifications(Good good, RelatedModification[] modifications) {
        
        if (modifications != null) {
            
            for (RelatedModification p : modifications) {
                
                switch (p.action) {
                
                    case CREATE: {
                        
                        Modification v = new Modification(); {
                            
                            v.setGood(good);
                            v.setPrice(p.price);
                            v.setSizeRange(p.sizeRange);
                            
                            em.persist(v);
                            em.flush();
                            
                            security.register(v, ResourceUtils.alias(null, "modifications/i", v));
                            
                            attributes.createAttributes(v, p.attributes);
                        }
                        break;
                    }
                    case UPDATE: {
                        
                        Modification v = em.find(Modification.class, p.id); {
                            
                            if (good.getId() != v.getGood().getId()) {
                                throw new WebApplicationException("Modification belongs to another good", Response.Status.CONFLICT);
                            }
                            
                            v.setPrice(p.price);
                            v.setSizeRange(p.sizeRange);
                            
                            em.merge(v);
                            em.flush();
                            
                            attributes.updateAttributes(v, p.attributes);
                        }
                        break;
                    }
                    case REMOVE: {
                        
                        Modification v = em.find(Modification.class, p.id);
                        em.remove(v);
                        break;
                    }
                default:
                    break;
                }
            }
            
            em.flush();
        }
    }
}
