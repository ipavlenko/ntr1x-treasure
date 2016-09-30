package com.ntr1x.treasure.web.services;

import java.util.Arrays;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p1.Image;
import com.ntr1x.treasure.web.model.p2.ResourceImage;

@Service
public class ImageService implements IImageService {
    
    @Inject
    private EntityManager em;
    
    @Override
    public void createImages(Resource resource, RelatedImage[] images) {
        
        if (images != null) {
            
            for (RelatedImage p : images) {
                
                ResourceImage v = new ResourceImage(); {
                    
                    Image e = em.find(Image.class, p.image);
                    
                    v.setRelate(resource);
                    v.setImage(e);
                    v.setAspects(p.aspects != null ? Arrays.asList(p.aspects) : null);
                    
                    em.persist(v);
                }
            }
            
            em.flush();
        }
    }
    
    @Override
    public void updateImages(Resource resource, RelatedImage[] images) {
        
        if (images != null) {
            
            for (RelatedImage p : images) {
                
                switch (p.action) {
                
                    case CREATE: {
                        
                        ResourceImage v = new ResourceImage(); {
                            
                            Image e = em.find(Image.class, p.image);
                            
                            v.setRelate(resource);
                            v.setImage(e);
                            v.setAspects(p.aspects != null ? Arrays.asList(p.aspects) : null);
                            
                            em.persist(v);
                        }
                        break;
                    }
                    case UPDATE: {
                        // ignore
                        break;
                    }
                    case REMOVE: {
                        
                        ResourceImage v = em.find(ResourceImage.class, p.id);
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
