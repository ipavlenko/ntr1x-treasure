package com.ntr1x.treasure.web;

import com.ntr1x.treasure.web.providers.AppConverterProvider;
import com.ntr1x.treasure.web.providers.AppJsonContextResolver;
import io.swagger.jaxrs.config.BeanConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AppConfig extends ResourceConfig {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config {
        public String host;
        public String scheme;
        public String basePath;
        public String version;
    }

	public AppConfig() {

        try {

            Context context = new InitialContext();
            Config config = (Config) context.lookup("java:app/config/AppConfig");

            register(MultiPartFeature.class);
            register(MoxyXmlFeature.class);
            register(MoxyJsonFeature.class);
            register(RolesAllowedDynamicFeature.class);

            register(AppConverterProvider.class);
            register(AppJsonContextResolver.class);

            packages("com.ntr1x.treasure.web.resources");

            BeanConfig beanConfig = new BeanConfig();
            beanConfig.setVersion(config.getVersion());
            beanConfig.setSchemes(new String[]{config.getScheme()});
            beanConfig.setHost(config.getHost());
            beanConfig.setBasePath(config.getBasePath());
            beanConfig.setResourcePackage("com.ntr1x.treasure.web.resources");
            beanConfig.setScan(true);

//		property(AppRouter.class.getName(), new AppRouter(this));
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }


	}
}

