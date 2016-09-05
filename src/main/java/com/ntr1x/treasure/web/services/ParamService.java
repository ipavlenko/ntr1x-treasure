package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.attributes.AttributeEntity;
import com.ntr1x.treasure.web.model.attributes.AttributeValue;
import com.ntr1x.treasure.web.model.security.SecurityResource;

@Service
public class ParamService implements IParamService {

    @Inject
    private EntityManager em;
    
    @Override
    public void createParams(SecurityResource resource, CreateParam[] params) {
        
        for (CreateParam p : params) {
            
            AttributeValue v = new AttributeValue(); {
                
                AttributeEntity e = em.find(AttributeEntity.class, p.attribute);
                
                v.setRelate(resource);
                v.setValue(p.value);
                v.setAttribute(e);
                
                em.persist(v);
            }
        }
        
        em.flush();
    }
    
    @Override
    public void updateParams(SecurityResource resource, UpdateParam[] params) {
        
        for (UpdateParam p : params) {
            
            switch (p.action) {
            
                case ADD: {
                    
                    AttributeValue v = new AttributeValue(); {
                        
                        AttributeEntity e = em.find(AttributeEntity.class, p.attribute);
                        
                        v.setRelate(resource);
                        v.setValue(p.value);
                        v.setAttribute(e);
                        
                        em.persist(v);
                    }
                    break;
                }
                case UPDATE: {
                    
                    AttributeValue v = em.find(AttributeValue.class, p.id); {
                        
                      v.setValue(p.value);
                      em.merge(v);
                    }
                    
                    em.merge(v);
                    break;
                }
                case REMOVE: {
                    
                    AttributeValue v = em.find(AttributeValue.class, p.id); {
                        
                        v.setValue(p.value);
                        em.remove(v);
                    }
                    break;
                }
            }
        }
        
        em.flush();
    }
}
