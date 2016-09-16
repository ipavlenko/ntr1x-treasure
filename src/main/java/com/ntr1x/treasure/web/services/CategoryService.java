package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p1.Category;
import com.ntr1x.treasure.web.model.p2.ResourceCategory;

@Service
public class CategoryService implements ICategoryService {

    @Inject
    private EntityManager em;
    
    @Override
    public void createCategories(Resource resource, RelatedCategory[] categories) {
        
        if (categories != null) {
            
            for (RelatedCategory p : categories) {
                
                ResourceCategory v = new ResourceCategory(); {
                    
                    Category e = em.find(Category.class, p.category);
                    
                    v.setRelate(resource);
                    v.setCategory(e);
                    
                    em.persist(v);
                }
            }
            
            em.flush();
        }
    }
    
    @Override
    public void updateCategories(Resource resource, RelatedCategory[] categories) {
        
        if (categories != null) {
            
            for (RelatedCategory p : categories) {
                
                switch (p.action) {
                
                    case CREATE: {
                        
                        ResourceCategory v = new ResourceCategory(); {
                            
                            Category e = em.find(Category.class, p.category);
                            
                            v.setRelate(resource);
                            v.setCategory(e);
                            
                            em.persist(v);
                        }
                        break;
                    }
                    case UPDATE: {
                        // ignore
                        break;
                    }
                    case REMOVE: {
                        
                        ResourceCategory v = em.find(ResourceCategory.class, p.id);
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
