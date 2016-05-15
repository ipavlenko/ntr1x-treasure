package com.ntr1x.treasure.web.oauth;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;

import com.ntr1x.treasure.web.oauth.IOAuthService.Credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApplicationScoped
public class OAuth2FacebookFactory implements IOAuthFactory {

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Config {
		
		public String authorizationEndpoint;
		public String tokenEndpoint;
		public String userinfoEndpoint;
	}
	
	@Resource(lookup = "java:app/config/FacebookFactory")
	private Config config;
	
	@Override
	public IOAuth2FacebookService create(Credentials credentials) {
		return new OAuth2FacebookService(config, credentials);
	}
}
