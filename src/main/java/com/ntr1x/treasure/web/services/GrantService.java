package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.model.p2.Grant;

@Service
public class GrantService implements IGrantService {

    @Inject
    private EntityManager em;
    
    
    @Override
    public void createGrants(User user, CreateGrant[] grants) {
        
        if (grants != null) {
            
            for (CreateGrant p : grants) {
                
                Grant v = new Grant(); {
                    
                    v.setUser(user);
                    v.setPattern(p.pattern);
                    v.setAction(p.allow);
                    
                    em.persist(v);
                }
            }
            
            em.flush();
        }
    }

    @Override
    public void updateGrants(User user, UpdateGrant[] grants) {
        
        if (grants != null) {
            
            for (UpdateGrant p : grants) {
                
                switch (p.action) {
                
                    case CREATE: {
                        
                        Grant v = new Grant(); {
                            
                            v.setUser(user);
                            v.setPattern(p.pattern);
                            v.setAction(p.allow);
                            
                            em.persist(v);
                        }
                        break;
                    }
                    case UPDATE: {
                        
                        Grant v = em.find(Grant.class, p.id); {
                          
                            if (v.getUser().getId() != user.getId()) {
                                throw new WebApplicationException("Grant belongs to another user", Response.Status.CONFLICT);
                            }
                            
                            v.setPattern(p.pattern);
                            v.setAction(p.allow);
                            
                            em.merge(v);
                        }
                        
                        break;
                    }
                    case REMOVE: {
                        
                        Grant v = em.find(Grant.class, p.id); {
                            
                            if (v.getUser().getId() != user.getId()) {
                                throw new WebApplicationException("Grant belongs to another user", Response.Status.CONFLICT);
                            }
                            
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
