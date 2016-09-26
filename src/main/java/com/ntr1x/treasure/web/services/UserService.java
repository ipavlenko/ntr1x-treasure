package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.UserRepository;

@Service
public class UserService implements IUserService {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private UserRepository users;
    
    @Inject
    private IGrantService grants;
   
    @Inject
    private ISecurityService security;
    
    @Override
    public User create(CreateUser create) {
        
        User u = new User(); {
            
            int random = security.randomInt();
            
            u.setEmail(create.email);
            u.setPwdhash(security.hashPassword(random, create.password));
            u.setRandom(random);
            u.setConfirmed(create.confirmed);
            
            em.persist(u);
            em.flush();
            
            security.register(u, ResourceUtils.alias(null, "users/i", u));
            
            grants.createGrants(u, create.grants);
        }
        
        em.refresh(u);

        return u;
    }

    @Override
    public User update(long id, UpdateUser update) {
        
        User u = em.find(User.class, id); {
        
            int random = security.randomInt();
            
            u.setEmail(update.email);
            u.setPwdhash(security.hashPassword(random, update.password));
            u.setRandom(random);
            u.setConfirmed(update.confirmed);
            
            em.merge(u);
            em.flush();
        
            grants.updateGrants(u, update.grants);
        }
        
        em.refresh(u);
        
        return u;
    }
    
    @Override
    public User remove(long id) {
        
        User u = em.find(User.class, id);
        em.remove(u);
        em.flush();
        return u;
    }
    
    @Override
    public UsersResponse list(int page, int size) {
        
        Page<User> result = users.findAll(new PageRequest(page, size));
        
        return new UsersResponse(
            result.getTotalElements(),
            page,
            size,
            result.getContent()
        );
    }
    
    @Override
    public User select(long id) {
        
        User user = em.find(User.class, id);
        return user;
    }

    @Override
    public UserItem selectItem(long id) {
        User user = em.find(User.class, id);

        return new UserItem(
            user,
            user.getImages()
        );
    }

    public UserItem selectItemByEmail(String email){

        User user = users.findByEmail(email);

        return new UserItem(
            user,
            user.getImages()
        );
    }
}
