package com.ntr1x.treasure.web.filtering;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.Priority;
import javax.inject.Singleton;

import org.glassfish.jersey.message.filtering.spi.AbstractEntityProcessor;
import org.glassfish.jersey.message.filtering.spi.EntityGraph;
import org.glassfish.jersey.message.filtering.spi.EntityProcessor;

import com.google.common.collect.Sets;
import com.ntr1x.treasure.web.model.Resource.ResourceProperty;

@Singleton
@Priority(Integer.MAX_VALUE - 5000)
public class ResourceEntityProcessor extends AbstractEntityProcessor {

    protected Result process(final String fieldName, final Class<?> fieldClass, final Annotation[] fieldAnnotations,
                             final Annotation[] annotations, final EntityGraph graph) {

        Set<String> scopes = Sets.newHashSet();
        
        if (fieldName != null) {
            
            for (Annotation a : fieldAnnotations) {
                
                if (a.annotationType().isAnnotationPresent(ResourceFiltering.class)) {
                    scopes.add(a.annotationType().getName());
                }
            }
            
            if (scopes.isEmpty()) {
                scopes.add(ResourceProperty.class.getName());
            }
        }
        
        addFilteringScopes(fieldName, fieldClass, scopes, graph);

        return EntityProcessor.Result.APPLY;
    }

}
