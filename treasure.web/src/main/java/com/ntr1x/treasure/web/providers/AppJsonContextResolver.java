package com.ntr1x.treasure.web.providers;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
 
@Provider
public class AppJsonContextResolver implements ContextResolver<MoxyJsonConfig> {
 
    private final MoxyJsonConfig config;
 
    public AppJsonContextResolver() {
        config = new MoxyJsonConfig()
            .setAttributePrefix("")
            .setValueWrapper("$")
            .setFormattedOutput(true)
            .property(JAXBContextProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
    }
 
    @Override
    public MoxyJsonConfig getContext(Class<?> objectType) {
        return config;
    }

}