package com.ntr1x.treasure.web.oauth;

import javax.annotation.Resource;

import com.ntr1x.treasure.web.oauth.IOAuthService.Credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuth2GoogleFactory implements IOAuthFactory {
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Config {
		
		public String authorizationEndpoint;
		public String tokenEndpoint;
		public String userinfoEndpoint;
	}
	
	@Resource(lookup = "java:app/config/GoogleFactory")
	private Config config;
	
	@Override
	public IOAuth2GoogleService create(Credentials credentials) {
		return new OAuth2GoogleService(config, credentials);
	}
}
