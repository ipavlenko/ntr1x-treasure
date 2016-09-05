package com.ntr1x.treasure.web.filtering;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.message.filtering.spi.ScopeResolver;

import com.ntr1x.treasure.web.model.security.SecurityResource.ResourceProperty;

@Singleton
public class ResourceScopeResolver implements ScopeResolver {

    public static final String PREFIX = ResourceScopeResolver.class.getName() + "_";
    
    @Context
    private Configuration configuration;

    @Override
    public Set<String> resolve(final Annotation[] annotations) {
        
        Set<String> scopes = new HashSet<>();
        
        if (annotations != null) {
            
            for (Annotation annotation : annotations) {
                
                if (annotation.annotationType().isAnnotationPresent(ResourceFiltering.class)) {
                    scopes.add(annotation.annotationType().getName());
                }
            }
        }
        
        if (scopes.isEmpty()) {
            
            scopes.add(ResourceProperty.class.getName());
        }

        return scopes;
    }
}
