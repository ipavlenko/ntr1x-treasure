package com.ntr1x.treasure.web.oauth;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.oauth.IOAuthService.Credentials;

@Service
public class OAuth2GoogleFactory implements IOAuthFactory {
	
    @Configuration
    public static class Config {
        
        @Value("${app.oauth.google.authorizationEndpoint}")
        public String authorizationEndpoint;
        
        @Value("${app.oauth.google.tokenEndpoint}")
        public String tokenEndpoint;
        
        @Value("${app.oauth.google.userinfoEndpoint}")
        public String userinfoEndpoint;
    }
    
    @Inject
    private Config config;
	
	@Override
	public IOAuth2GoogleService create(Credentials credentials) {
		return new OAuth2GoogleService(config, credentials);
	}
}
