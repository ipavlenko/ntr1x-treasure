package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Bootstrap implements IBootstrap {
    
    @Value("${app.host}")
    private String host;
    
    @Inject
    private BootstrapHolder holder;
    
    @Inject
    private BootstrapUsers users;
    
    @Inject
    private BootstrapSessions sessions;

    @Inject
    private BootstrapCategories categories;
    
    @Inject
    private BootstrapMethods methods;
    
    @Inject
    private BootstrapProviders providers;
    
    @Inject
    private BootstrapPurchases purchases;
    
    public BootstrapResults bootstrap() {
        
        WebTarget target = ClientBuilder
            .newClient()
            .target(String.format("http://%s", host))
        ;
        
        BootstrapState state = holder.get();
        
        state.users = users.createUsers(target);
        state.sessions = sessions.createSessions(target);
        state.directories = categories.createDirectories(target);
        state.methods = methods.createMethods(target);
        state.providers = providers.createProviders(target);
        state.purchases = purchases.createPurchases(target);
        
        BootstrapResults results = new BootstrapResults(); {
            results.sessions = state.sessions;
        }
        
        return results;
    }
}
