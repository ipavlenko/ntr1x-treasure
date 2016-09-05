package com.ntr1x.treasure.web.oauth;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.oauth.IOAuthService.Credentials;

@Service
public class OAuth2FacebookFactory implements IOAuthFactory {

	@Configuration
	public static class Config {
		
	    @Value("${app.oauth.facebook.authorizationEndpoint}")
		public String authorizationEndpoint;
	    
	    @Value("${app.oauth.facebook.tokenEndpoint}")
        public String tokenEndpoint;
		
	    @Value("${app.oauth.facebook.userinfoEndpoint}")
        public String userinfoEndpoint;
	}
	
	@Inject
	private Config config;
	
	@Override
	public IOAuth2FacebookService create(Credentials credentials) {
		return new OAuth2FacebookService(config, credentials);
	}
}
