package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p2.Method;
import com.ntr1x.treasure.web.services.IMethodService;

@Service
public class BootstrapMethods {

    @Inject
    private BootstrapHolder holder;
    
    public BootstrapState.Methods createMethods(WebTarget target) {
        
        BootstrapState state = holder.get();
        
        BootstrapState.Methods methods = new BootstrapState.Methods();
        
        {
            IMethodService.MethodCreate s = new IMethodService.MethodCreate(); {
                s.title = "Наличными";
                s.user = state.users.seller1.getId();
            }
            
            methods.seller1MethodCash = target
                .path("/me/methods")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller1)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Method.class)
            ;
        }
        
        {
            IMethodService.MethodCreate s = new IMethodService.MethodCreate(); {
                s.title = "На карту";
                s.user = state.users.seller1.getId();
            }
            
            methods.seller1MethodCard = target
                .path("/me/methods")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller1)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Method.class)
            ;
        }
        
        {
            IMethodService.MethodCreate s = new IMethodService.MethodCreate(); {
                s.title = "Наличными";
                s.user = state.users.seller2.getId();
            }
            
            methods.seller2MethodCash = target
                .path("/me/methods")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller2)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Method.class)
            ;
        }
        
        {
            IMethodService.MethodCreate s = new IMethodService.MethodCreate(); {
                s.title = "На карту";
                s.user = state.users.seller2.getId();
            }
            
            methods.seller2MethodCard = target
                .path("/me/methods")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller2)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Method.class)
            ;
        }
        
        return methods;
    }
}
