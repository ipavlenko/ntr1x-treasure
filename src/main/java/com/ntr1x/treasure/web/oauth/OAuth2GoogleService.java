package com.ntr1x.treasure.web.oauth;

import java.net.URI;
import java.util.UUID;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuth2GoogleService implements IOAuth2GoogleService {
	
	private final OAuth2GoogleFactory.Config config;
	private final Credentials credentials;
	
	public URI auth(String token) {
		
		return ClientBuilder.newClient()
			.target(config.authorizationEndpoint)
			.getUriBuilder()
				.queryParam("scope", "openid email profile")
				.queryParam("state", token)
				.queryParam("redirect_uri", credentials.redirectUri)
				.queryParam("response_type", "code")
				.queryParam("client_id", credentials.clientId)
				.queryParam("include_granted_scopes", true)
				.build()
		;
	}
	
	@Override
	public GoogleRequestToken token() {
		return new GoogleRequestToken(UUID.randomUUID().toString().replace("-", ""));
	}
	
	@Override
	public GoogleAccessToken token(String token, String verifier) {
		
		return ClientBuilder.newClient()
			.register(MoxyXmlFeature.class)
			.register(MoxyJsonFeature.class)
			.register(new LoggingFilter())
			.target(config.tokenEndpoint)
			.request(MediaType.APPLICATION_JSON_TYPE)
				.post(
					Entity.entity(
						new Form()
							.param("code", verifier)
							.param("client_id", credentials.clientId)
							.param("client_secret", credentials.clientSecret)
							.param("redirect_uri", credentials.redirectUri)
							.param("grant_type", "authorization_code")
						,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE
					),
					GoogleAccessToken.class
				)
		;
	}
	
	@Override
	public GoogleUserInfo userinfo(AccessToken token) {
		
		return ClientBuilder.newClient()
			.register(MoxyXmlFeature.class)
			.register(MoxyJsonFeature.class)
			.register(new LoggingFilter())
			.target(config.userinfoEndpoint)
			.request(MediaType.APPLICATION_JSON_TYPE)
			.header("Authorization", String.format("Bearer %s", token.value()))
			.get(GoogleUserInfo.class)
		;
	}

//	public static void main(String[] args) throws Exception {
//
//		OAuth2GoogleService google = new OAuth2GoogleService(
//
//			new Config(
//				"https://accounts.google.com/o/oauth2/auth",
//				"https://accounts.google.com/o/oauth2/token",
//				"https://www.googleapis.com/plus/v1/people/me/openIdConnect",
//				"574353130463-3lmo3o8cfgo3gronqpnuh1612tn2k71a.apps.googleusercontent.com",
//				"MP1YclXDzMC4ve3UaUJcbqUv",
//				"https://en.bookagolf.com/oauth/google/callback",
//				""
//			)
//		);
//
//		PrintStream out = System.out;
//		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//
//		out.println("Google Authorization Flow");
//		out.println("Input State:");
//		String state = in.readLine();
//
//		URI uri = google.auth(state);
//		out.println("Authorization URL:");
//		out.println(uri.toString());
//
//		out.println("Input Authorization Code:");
//		String code = in.readLine();
//
//		GoogleAccessToken token = google.token(state, code);
//		out.println("Access Token:");
//		out.println(token);
//
//		GoogleUserInfo userinfo = google.userinfo(token);
//		out.println("User Info:");
//		out.println(userinfo);
//	}
}
