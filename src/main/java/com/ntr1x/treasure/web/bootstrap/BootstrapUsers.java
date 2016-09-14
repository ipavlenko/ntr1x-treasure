package com.ntr1x.treasure.web.bootstrap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.bootstrap.BootstrapState.Users;
import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.model.User.Role;
import com.ntr1x.treasure.web.services.IUserService;

@Service
public class BootstrapUsers {
    
    public Users createUsers(WebTarget target) {
        
        Users users = new Users();
        
        {    
            IUserService.CreateUser u = new IUserService.CreateUser(); {
                
                u.role = Role.ADMIN;
                u.confirmed = true;
                u.email = "admin@example.com";
                u.password = "admin";
                u.phone = "00000000000";
                u.surname = "admin";
                u.name = "admin";
                u.middlename = "admin";
                u.confirmed = true;
            }
            
            users.admin = target
              .path("/ws/users")
              .request(MediaType.APPLICATION_JSON_TYPE)
              .post(Entity.entity(u, MediaType.APPLICATION_JSON_TYPE), User.class)
            ;
            
            users.adminPassword = u.password;
        }
        
        {    
            IUserService.CreateUser u = new IUserService.CreateUser(); {
                
                u.role = Role.USER;
                u.confirmed = true;
                u.email = "user@example.com";
                u.password = "user";
                u.phone = "11111111111";
                u.surname = "user";
                u.name = "user";
                u.middlename = "user";
                u.confirmed = true;
            }
            
            users.user = target
              .path("/ws/users")
              .request(MediaType.APPLICATION_JSON_TYPE)
              .post(Entity.entity(u, MediaType.APPLICATION_JSON_TYPE), User.class)
            ;
            
            users.userPassword = u.password;
        }
        
        return users;
    }
}
