package com.ntr1x.treasure.web.filtering;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.jersey.message.filtering.EntityFilteringFeature;

public class ResourceFilteringFeature implements Feature {

    @Override
    public boolean configure(final FeatureContext context) {
        
        Configuration config = context.getConfiguration();

        if (!config.isRegistered(ResourceEntityProcessor.class)) {
            
            if (!config.isRegistered(EntityFilteringFeature.class)) {
                context.register(EntityFilteringFeature.class);
            }
            
            context.register(ResourceEntityProcessor.class);
            context.register(ResourceScopeResolver.class);

            return true;
        }
        return true;
    }
}
