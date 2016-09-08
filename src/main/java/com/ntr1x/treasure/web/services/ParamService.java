package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.Attribute;
import com.ntr1x.treasure.web.model.Resource;
import com.ntr1x.treasure.web.model.ResourceAttribute;

@Service
public class ParamService implements IParamService {

    @Inject
    private EntityManager em;
    
    @Override
    public void createParams(Resource resource, CreateParam[] params) {
        
        if (params != null) {
            
            for (CreateParam p : params) {
                
                ResourceAttribute v = new ResourceAttribute(); {
                    
                    Attribute e = em.find(Attribute.class, p.attribute);
                    
                    v.setRelate(resource);
                    v.setValue(p.value);
                    v.setAttribute(e);
                    
                    em.persist(v);
                }
            }
            
            em.flush();
        }
    }
    
    @Override
    public void updateParams(Resource resource, UpdateParam[] params) {
        
        if (params != null) {
            
            for (UpdateParam p : params) {
                
                switch (p.action) {
                
                    case CREATE: {
                        
                        ResourceAttribute v = new ResourceAttribute(); {
                            
                            Attribute e = em.find(Attribute.class, p.attribute);
                            
                            v.setRelate(resource);
                            v.setValue(p.value);
                            v.setAttribute(e);
                            
                            em.persist(v);
                        }
                        break;
                    }
                    case UPDATE: {
                        
                        ResourceAttribute v = em.find(ResourceAttribute.class, p.id); {
                            
                          v.setValue(p.value);
                          em.merge(v);
                        }
                        
                        em.merge(v);
                        break;
                    }
                    case REMOVE: {
                        
                        ResourceAttribute v = em.find(ResourceAttribute.class, p.id); {
                            
                            v.setValue(p.value);
                            em.remove(v);
                        }
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
