package com.ntr1x.treasure.web.bootstrap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.ntr1x.treasure.web.model.Account;
import com.ntr1x.treasure.web.resources.AccountResource.AccountCreate;

public class BootstrapAccounts {
    
    public Accounts createAccounts(WebTarget target) {
        
        Accounts accounts = new Accounts();
        
        {
            AccountCreate s = new AccountCreate(); {
                s.email = "admin@example.com";
                s.password = "admin";
                s.grants = new AccountCreate.Grant[] {
                    new AccountCreate.Grant("/", "admin"),
                };
            }
            
            accounts.admin = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Account.class)
            ;
            accounts.adminPassword = s.password;
        }
        
        {
            AccountCreate s = new AccountCreate(); {
                s.email = "user@example.com";
                s.password = "user";
            }
            
            accounts.user = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Account.class)
            ;
            accounts.userPassword = s.password;
        }
        
        return accounts;
    }
    
    class Accounts {
        
        Account admin;
        String adminPassword;
        
        Account user;
        String userPassword;
    }
}
