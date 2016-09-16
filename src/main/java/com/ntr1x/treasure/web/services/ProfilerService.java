package com.ntr1x.treasure.web.services;

import java.util.function.Consumer;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.index.ModificationIndexRepository;
import com.ntr1x.treasure.web.resources.SecurityResource.Signin;
import com.ntr1x.treasure.web.resources.SecurityResource.SigninResponse;
import com.ntr1x.treasure.web.resources.SecurityResource.Signout;
import com.ntr1x.treasure.web.resources.SecurityResource.SignoutResponse;

import lombok.Getter;

@Service
public class ProfilerService implements IProfilerService {

    @Getter
    private boolean securityDisabled = true;
    
    @Inject
    private ModificationIndexRepository modifications;

    @Override
    public void withDisabledSecurity(Runnable runnable) {
        
        try {
            securityDisabled = true;
            runnable.run();
        } finally {
            securityDisabled = false;
        }
    }
    
    private String createSession(WebTarget target, String email, String password) {
        
        Signin s = new Signin(email, password);
        
        SigninResponse r = target
            .path("/security/signin")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), SigninResponse.class)
        ;
        
        return r == null ? null : r.token;
    }
    
    private void removeSession(WebTarget target, String token) {
        
        Signout s = new Signout();
        
        target
            .path("/security/signout")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .header(HttpHeaders.AUTHORIZATION, token)
            .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), SignoutResponse.class)
        ;
    }

    @Override
    public void withCredentials(WebTarget target, String email, String password, Consumer<String> consumer) {
        
        String token = null;
        try {
            
            try {
                
                token = createSession(target, email, password);
            
            } catch (Exception e) {
                // ignore
            }
            
            consumer.accept(token);
        
        } finally {
            
            if (token != null) {
                
                try {
                    
                    removeSession(target, token);
                
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public void clearSearchIndex() {
        modifications.deleteAll();
    }
}
