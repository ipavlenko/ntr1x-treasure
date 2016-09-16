package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;

import org.springframework.stereotype.Service;

@Service
public class BootstrapAttributes {
    
    @Inject
    private BootstrapHolder holder;
    
    public BootstrapState.Attributes createAttributes(WebTarget target) {
        
        BootstrapState state = holder.get();
        
        BootstrapState.Attributes attributes = new BootstrapState.Attributes();
        
        
        
        return attributes;
    }
}
