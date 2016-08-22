package com.ntr1x.treasure.web;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.message.filtering.SecurityEntityFilteringFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.converter.AppConverterProvider;
import com.ntr1x.treasure.web.filters.AuthenticationFilter;
import com.ntr1x.treasure.web.filters.AuthorizationFilter;
import com.ntr1x.treasure.web.filters.CORSRequestFilter;
import com.ntr1x.treasure.web.filters.CORSResponseFilter;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@Component
public class JerseyConfig extends ResourceConfig {

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
		register(SecurityEntityFilteringFeature.class);
		register(AuthenticationFilter.class);
		register(AuthorizationFilter.class);
        
		
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.0");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost("localhost:8080");
		beanConfig.setBasePath("");
		beanConfig.setResourcePackage("com.ntr1x.treasure.web.resources");
		beanConfig.setScan(true);
	}
}