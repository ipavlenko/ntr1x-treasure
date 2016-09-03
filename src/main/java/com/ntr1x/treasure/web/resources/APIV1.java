package com.ntr1x.treasure.web.resources;

import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.ReaderListener;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;

@SwaggerDefinition(
	info = @Info(
		title = "Treasure API",
		description = "Universal data storage platform for web developers",
		version = "1.0.0"
	),
	schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS },
	tags = {
        @Tag(name = "Setup",        description = "Applciation setup"),
	    @Tag(name = "Security",     description = "Registration & Authorization"),
		@Tag(name = "Resources",    description = "Universal access to the application data"),
		@Tag(name = "Publications", description = "News, events, blogs, etc."),
		@Tag(name = "Categories",   description = "Resource catgories"),
		@Tag(name = "Accounts",     description = "Users & Profiles"),
//		@Tag(name = "Goods",        description = "Goods, services, offers, etc."),
	}
)
public class APIV1 implements ReaderListener {

    @Override
    public void beforeScan(Reader reader, Swagger swagger) {
    }

    @Override
    public void afterScan(Reader reader, Swagger swagger) {

        ApiKeyAuthDefinition  tokenScheme = new ApiKeyAuthDefinition ();
        tokenScheme.setType("apiKey");
        tokenScheme.setName("Authorization");
        tokenScheme.setIn(In.HEADER);
        swagger.addSecurityDefinition("api_key", tokenScheme);
    }
}