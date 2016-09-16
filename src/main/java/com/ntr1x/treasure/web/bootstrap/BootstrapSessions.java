package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.bootstrap.BootstrapState.Sessions;
import com.ntr1x.treasure.web.resources.SecurityResource.Signin;
import com.ntr1x.treasure.web.resources.SecurityResource.SigninResponse;

@Service
public class BootstrapSessions {

    @Inject
    private BootstrapHolder holder;
    
    public Sessions createSessions(WebTarget target) {
        
        BootstrapState state = holder.get();
        
        Sessions sessions = new Sessions();
        
        {
            Signin s = new Signin(state.users.admin.getEmail(), state.users.adminPassword);
            
            SigninResponse r = target
                .path("/security/signin")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), SigninResponse.class)
            ;
            
            sessions.admin = r == null ? null : r.token;
        }
        
        {
            Signin s = new Signin(state.users.user1.getEmail(), state.users.user1Password);
            
            SigninResponse r = target
                .path("/security/signin")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), SigninResponse.class)
            ;
            
            sessions.user1 = r == null ? null : r.token;
        }
        
        {
            Signin s = new Signin(state.users.user2.getEmail(), state.users.user2Password);
            
            SigninResponse r = target
                .path("/security/signin")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), SigninResponse.class)
            ;
            
            sessions.user2 = r == null ? null : r.token;
        }
        
        {
            Signin s = new Signin(state.users.seller1.getEmail(), state.users.seller1Password);
            
            SigninResponse r = target
                .path("/security/signin")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), SigninResponse.class)
            ;
            
            sessions.seller1 = r == null ? null : r.token;
        }
        
        {
            Signin s = new Signin(state.users.seller2.getEmail(), state.users.seller2Password);
            
            SigninResponse r = target
                .path("/security/signin")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), SigninResponse.class)
            ;
            
            sessions.seller2 = r == null ? null : r.token;
        }
        
        return sessions;
    }
}
