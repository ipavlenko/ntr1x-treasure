package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p2.Provider;
import com.ntr1x.treasure.web.services.IProviderService;

@Service
public class BootstrapProviders {

    @Inject
    private BootstrapHolder holder;
    
    public BootstrapState.Providers createProviders(WebTarget target) {
        
        BootstrapState state = holder.get();
        
        BootstrapState.Providers providers = new BootstrapState.Providers();
        
        {
            IProviderService.ProviderCreate s = new IProviderService.ProviderCreate(); {
                s.title = "Китайские поставки";
                s.user = state.users.seller1.getId();
            }
            
            providers.seller1China = target
                .path("/me/providers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller1)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Provider.class)
            ;
        }
        
        {
            IProviderService.ProviderCreate s = new IProviderService.ProviderCreate(); {
                s.title = "Индийские поставки";
                s.user = state.users.seller1.getId();
            }
            
            providers.seller1India = target
                .path("/me/providers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller1)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Provider.class)
            ;
        }
        
        {
            IProviderService.ProviderCreate s = new IProviderService.ProviderCreate(); {
                s.title = "Китайские поставки";
                s.user = state.users.seller2.getId();
            }
            
            providers.seller2China = target
                .path("/me/providers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller2)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Provider.class)
            ;
        }
        
        {
            IProviderService.ProviderCreate s = new IProviderService.ProviderCreate(); {
                s.title = "Японские поставки";
                s.user = state.users.seller2.getId();
            }
            
            providers.seller2Japan = target
                .path("/me/providers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller2)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Provider.class)
            ;
        }
        
        return providers;
    }
}
