package com.ntr1x.treasure.web.services;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.model.p1.User.Role;
import com.ntr1x.treasure.web.model.p2.Cart;
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
    private IAttributeService attributes;
    
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
            u.setRegistered(LocalDateTime.now());
            u.setPhone(create.phone);
            u.setName(create.name);
            u.setSurname(create.surname);
            u.setMiddlename(create.middlename);
            u.setRole(create.role);
            
            em.persist(u);
            em.flush();
            
            security.register(u, ResourceUtils.alias(null, "users/i", u));
            
            attributes.createAttributes(u, create.attributes);
            grants.createGrants(u, create.grants);
        }
        
        Cart c = new Cart(); {
            
            c.setUser(u);
            
            em.persist(c);
            em.flush();
            
            security.register(u, ResourceUtils.alias(null, "carts/i", c));
            
            em.merge(c);
            em.flush();
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
            u.setPhone(update.phone);
            u.setName(update.name);
            u.setSurname(update.surname);
            u.setMiddlename(update.middlename);
            u.setRole(update.role);
            
            em.merge(u);
            em.flush();
        
            attributes.updateAttributes(u, update.attributes);
            grants.updateGrants(u, update.grants);
        }
        
        em.refresh(u);
        
        return u;
    }
    
    @Override
    public User remove(long id) {
        
        User u = em.find(User.class, id);
        em.remove(u);
        return u;
    }
    
    @Override
    public UsersResponse list(int page, int size, Role role) {
        
        Page<User> result = users.findUsersByRole(role, new PageRequest(page, size));
        
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
}
