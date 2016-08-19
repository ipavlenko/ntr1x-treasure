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
		@Tag(name = "Resources",    description = "Universal access to the application data"),
		@Tag(name = "Publications", description = "News, events, blogs, etc."),
		@Tag(name = "Goods", description = "Goods, services, offers, etc."),
		@Tag(name = "Accounts", description = "Users & Profiles."),
	}
)
public interface APIV1 {
}