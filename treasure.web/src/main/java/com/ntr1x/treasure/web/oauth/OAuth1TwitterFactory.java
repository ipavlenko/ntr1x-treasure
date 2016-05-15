package com.ntr1x.treasure.web.oauth;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;

import com.ntr1x.treasure.web.oauth.IOAuthService.Credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApplicationScoped
public class OAuth1TwitterFactory implements IOAuthFactory {

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Config {
		
		public String authorizationEndpoint;
		public String requestTokenEndpoint;
		public String accessTokenEndpoint;
		public String userinfoEndpoint;
	}
	
	@Resource(lookup = "java:app/config/TwitterFactory")
	private Config config;
	
	@Override
	public IOAuth1TwitterService create(Credentials credentials) {
		return new OAuth1TwitterService(config, credentials);
	}
}
