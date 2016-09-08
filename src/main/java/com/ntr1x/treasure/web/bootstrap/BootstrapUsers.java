package com.ntr1x.treasure.web.bootstrap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.Aspect;
import com.ntr1x.treasure.web.model.User.Role;
import com.ntr1x.treasure.web.resources.UsersResource.CreateUser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
public class BootstrapUsers {
    
    public Users createUsers(WebTarget target) {
        
        Users users = new Users();
        
        {    
            CreateUser u = new CreateUser(); {
                
                u.role = Role.ADMIN;
                u.confirmed = true;
                u.email = "admin@example.com";
                u.password = "admin";
                u.phone = "00000000000";
                u.surname = "admin";
                u.userName = "admin";
                u.middleName = "admin";
                u.confirmed = true;
            }
            
            users.admin = target
              .path("/ws/users")
              .request(MediaType.APPLICATION_JSON_TYPE)
              .post(Entity.entity(u, MediaType.APPLICATION_JSON_TYPE), SecurityUser.class)
            ;
            
            users.adminPassword = u.password;
        }
        
        {    
            CreateUser u = new CreateUser(); {
                
                u.role = Role.USER;
                u.type = Aspect.EXTENDED;
                u.confirmed = true;
                u.email = "user@example.com";
                u.password = "user";
                u.phone = "11111111111";
                u.surname = "user";
                u.userName = "user";
                u.middleName = "user";
                u.confirmed = true;
            }
            
            users.user = target
              .path("/ws/users")
              .request(MediaType.APPLICATION_JSON_TYPE)
              .post(Entity.entity(u, MediaType.APPLICATION_JSON_TYPE), SecurityUser.class)
            ;
            
            users.userPassword = u.password;
        }
        
        return users;
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
