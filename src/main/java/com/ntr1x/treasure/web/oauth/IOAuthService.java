package com.ntr1x.treasure.web.oauth;

import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface IOAuthService {
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Credentials {
		
		public String clientId;
		public String clientSecret;
		public String redirectUri;
	}
	
	URI auth(String token);
	RequestToken token();
	AccessToken token(String token, String verifier);
	UserInfo userinfo(AccessToken token);
	
	public interface RequestToken {
		
		String value();
		String secret();
	}
	
	public interface AccessToken {
		
		String value();
		String secret();
	}
	
	public interface UserInfo {
		
		String source();
		String reference();
		String name();
		String email();
		String surname();
	}
}
