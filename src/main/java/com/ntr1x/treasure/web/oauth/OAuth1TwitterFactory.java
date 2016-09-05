package com.ntr1x.treasure.web.oauth;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.oauth.IOAuthService.Credentials;

@Service
public class OAuth1TwitterFactory implements IOAuthFactory {

    @Configuration
	public static class Config {
		
        @Value("${app.oauth.twitter.authorizationEndpoint}")
		public String authorizationEndpoint;
        
        @Value("${app.oauth.twitter.requestTokenEndpoint}")
		public String requestTokenEndpoint;
        
        @Value("${app.oauth.twitter.accessTokenEndpoint}")
		public String accessTokenEndpoint;
        
        @Value("${app.oauth.twitter.userinfoEndpoint}")
		public String userinfoEndpoint;
	}
	
	@Inject
	private Config config;
	
	@Override
	public IOAuth1TwitterService create(Credentials credentials) {
		return new OAuth1TwitterService(config, credentials);
	}
}
