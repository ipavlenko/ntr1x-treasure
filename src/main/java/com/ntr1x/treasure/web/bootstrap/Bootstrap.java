package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.services.IProfilerService;

@Service
public class Bootstrap implements IBootstrap {
    
    @Value("${app.host}")
    private String host;
    
    @Inject
    private IProfilerService profiler;

    @Inject
    private BootstrapHolder holder;
    
    @Inject
    private BootstrapUsers users;

    @Inject
    private BootstrapCategories categories;
    
    @Inject
    private BootstrapPurchases purchases;
    
    public BootstrapResults bootstrap() {
        
        WebTarget target = ClientBuilder
            .newClient()
            .target(String.format("http://%s", host))
        ;
        
        BootstrapState state = holder.get();
        
        BootstrapResults results = new BootstrapResults();
        
        profiler.withDisabledSecurity(() -> {
            state.users = users.createUsers(target);
        });
        
        profiler.withCredentials(target, state.users.admin.getEmail(), state.users.adminPassword, (token) -> {
            results.adminToken = token;
        });
        
        profiler.withCredentials(target, state.users.user.getEmail(), state.users.userPassword, (token) -> {
            results.userToken = token;
        });
        
        profiler.withCredentials(target, state.users.admin.getEmail(), state.users.adminPassword, (token) -> {
            state.directories = categories.createDirectories(target, token);
            state.purchases = purchases.createPurchases(target, token);
        });
        
        return results;
    }
}
