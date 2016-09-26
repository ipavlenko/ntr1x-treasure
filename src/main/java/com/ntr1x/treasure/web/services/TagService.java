package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p1.Tag;

@Service
public class TagService implements ITagService {

    @Inject
    private EntityManager em;
    
    @Override
    public void createTags(Resource resource, RelatedTag[] tags) {
        
        if (tags != null) {
            
            for (RelatedTag p : tags) {
                
                Tag v = new Tag(); {
                    
                    v.setRelate(resource);
                    v.setValue(p.value);
                    
                    em.persist(v);
                }
            }
            
            em.flush();
        }
    }
    
    @Override
    public void updateTags(Resource resource, RelatedTag[] tags) {
        
        if (tags != null) {
            
            for (RelatedTag p : tags) {
                
                switch (p.action) {
                
                    case CREATE: {
                        
                        Tag v = new Tag(); {
                            
                            v.setRelate(resource);
                            v.setValue(p.value);
                            
                            em.persist(v);
                        }
                        break;
                    }
                    case UPDATE: {
                        
                        Tag v = em.find(Tag.class, p.id); {
                            
                            if (resource.getId() != v.getRelate().getId()) {
                                throw new WebApplicationException("Tag belongs to another resource", Response.Status.CONFLICT);
                            }
                            
                            v.setValue(p.value);
                            
                            em.merge(v);
                            em.flush();
                        }
                        break;
                    }
                    case REMOVE: {
                        
                        Tag v = em.find(Tag.class, p.id); {
                            em.remove(v);
                        }
                        break;
                    }
                }
            }
            
            em.flush();
        }
    }
}
