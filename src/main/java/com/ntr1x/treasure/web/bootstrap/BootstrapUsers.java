package com.ntr1x.treasure.web.bootstrap;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.client.WebTarget;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.security.SecurityUser;
import com.ntr1x.treasure.web.model.security.SecurityUser.Role;
import com.ntr1x.treasure.web.services.ISecurityService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
public class BootstrapUsers {
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private EntityManager em;
    
    public Users createUsers(WebTarget target) {
        
        int random = security.randomInt();
        
        SecurityUser admin = new SecurityUser(
            true,
            true,
            random,
            Timestamp.valueOf(LocalDateTime.now()),
            "admin@examle.com",
            "79001234567",
            "admin",
            "Admin",
            "Admin",
            security.hashPassword(random, "admin"),
            Role.ADMIN,
            null,
            null,
            null
        );
        
        em.persist(admin);
        em.flush();
        
        admin.setName(String.format("/users/%s", admin.getId()));
        
        em.merge(admin);
        em.flush();
        
        return new Users(
            admin,
            "admin",
            null,
            null
        );
    }
    
    @NoArgsConstructor
    @AllArgsConstructor
    class Users {
        
        public SecurityUser admin;
        public String adminPassword;
        
        public SecurityUser user;
        public String userPassword;
    }
}
