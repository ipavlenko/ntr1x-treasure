package com.ntr1x.treasure.web.bootstrap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.services.IGrantService;
import com.ntr1x.treasure.web.services.IUserService;

public class BootstrapUsers {
    
    public Users createAccounts(WebTarget target) {
        
        Users accounts = new Users();
        
        {
            IUserService.CreateUser s = new IUserService.CreateUser(); {
                s.email = "admin@example.com";
                s.password = "admin";
                s.confirmed = true;
                s.grants = new IGrantService.CreateGrant[] {
                    new IGrantService.CreateGrant("/", "admin"),
                };
            }
            
            accounts.admin = target
                .path("/users")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), User.class)
            ;
            accounts.adminPassword = s.password;
        }
        
        {
            IUserService.CreateUser s = new IUserService.CreateUser(); {
                s.email = "user@example.com";
                s.password = "user";
                s.confirmed = true;
            }
            
            accounts.user = target
                .path("/users")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), User.class)
            ;
            accounts.userPassword = s.password;
        }
        
        return accounts;
    }
    
    public static class Users {
        
        User admin;
        String adminPassword;
        
        User user;
        String userPassword;
    }
}
