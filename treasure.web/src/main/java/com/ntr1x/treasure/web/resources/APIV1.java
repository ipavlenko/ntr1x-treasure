package com.ntr1x.treasure.web.resources;

import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@SwaggerDefinition(
	info = @Info(
		title = "Treasure API",
		description = "Universal data storage platform for web developers",
		version = "1.0.0"
	),
	schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS },
	tags = {
		@Tag(name = "Media",    description = "Media catalog (goods, services)"),
		@Tag(name = "Persons",  description = "Persons catalog (personalities)"),
		@Tag(name = "Offers",   description = "Offers catalog (banners, offers)"),
        @Tag(name = "Entries",  description = "Entries catalog (goods, services)"),
//		@Tag(name = "Promos",   description = "Promos catalog (banners, offers)"),
//		@Tag(name = "Publications", description = "Publications catalog (news, events)"),
//		@Tag(name = "Objects",  description = "Objects catalog (goods, services)"),
//		@Tag(name = "Security", description = "Security management (portal users)"),
//		@Tag(name = "Portals",  description = "Portals management (supervisory control)"),
//		@Tag(name = "OAuthProviders", description = "OAuth security providers (supervisory control)"),
    }
)
public interface APIV1 {
}
