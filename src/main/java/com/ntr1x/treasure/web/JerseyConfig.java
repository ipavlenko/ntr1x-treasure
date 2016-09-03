package com.ntr1x.treasure.web;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.converter.AppConverterProvider;
import com.ntr1x.treasure.web.filtering.ResourceFilteringFeature;
import com.ntr1x.treasure.web.filters.AuthenticationFilter;
import com.ntr1x.treasure.web.filters.AuthorizationFilter;
import com.ntr1x.treasure.web.filters.CORSRequestFilter;
import com.ntr1x.treasure.web.filters.CORSResponseFilter;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@Component
@Configuration
public class JerseyConfig extends ResourceConfig {

	protected ServiceLocator serviceLocator;
	
	@Value("${app.host}")
    private String host;

	@Value("${app.schemes}")
    private String[] schemes;
	
	@Bean
	@Scope("singleton")
	public ServiceLocatorProvider getServiceLocator() {
	    return new ServiceLocatorProvider(this);
	}
	
    public JerseyConfig() {
		
		packages("com.ntr1x.treasure.web.resources");
		
		register(ApiListingResource.class);
		register(SwaggerSerializers.class);
		register(CORSRequestFilter.class);
		register(CORSResponseFilter.class);
		register(AppConverterProvider.class);
		register(MultiPartFeature.class);
		register(MoxyXmlFeature.class);
		register(MoxyJsonFeature.class);
		register(EntityFilteringFeature.class);
		register(ResourceFilteringFeature.class);
		register(RolesAllowedDynamicFeature.class);
		register(AuthenticationFilter.class);
		register(AuthorizationFilter.class);
		
		register(new ContainerLifecycleListener() {
		    
            public void onStartup(Container container) {
                serviceLocator = container.getApplicationHandler().getServiceLocator();
            }

            public void onReload(Container container) {
                serviceLocator = container.getApplicationHandler().getServiceLocator();
            }
            public void onShutdown(Container container) {
                serviceLocator = null;
            }
        });
		
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.0");
		beanConfig.setSchemes(schemes);
		beanConfig.setHost(host);
		beanConfig.setBasePath("");
		beanConfig.setResourcePackage("com.ntr1x.treasure.web.resources");
		beanConfig.setScan(true);
	}
    
    public static class ServiceLocatorProvider {
        
        private JerseyConfig config;

        private ServiceLocatorProvider(JerseyConfig config) {
            this.config = config;
        }
        
        public ServiceLocator get() {
            
            return config.serviceLocator;
        }
    }
}