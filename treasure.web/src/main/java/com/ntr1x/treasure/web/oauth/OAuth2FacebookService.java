package com.ntr1x.treasure.web.oauth;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import lombok.RequiredArgsConstructor;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

@RequiredArgsConstructor
public class OAuth2FacebookService implements IOAuth2FacebookService {

	private final OAuth2FacebookFactory.Config config;
	private final Credentials credentials;
	
	@Override
	public URI auth(String state) {
		
		return ClientBuilder.newClient()
			.target(config.authorizationEndpoint)
			.getUriBuilder()
				.queryParam("scope", "email public_profile")
				.queryParam("state", state)
				.queryParam("redirect_uri", credentials.redirectUri)
				.queryParam("response_type", "code")
				.queryParam("client_id", credentials.clientId)
				.build()
		;
	}
	
	@Override
	public FacebookRequestToken token() {
		return new FacebookRequestToken(UUID.randomUUID().toString().replace("-", ""));
	}

	@Override
	public FacebookAccessToken token(String requestToken, String verifier) {
		
		String body = ClientBuilder.newClient()
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
						,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE
					),
					String.class
				)
		;
		
		Map<String, String> data = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(body);
		
		return new FacebookAccessToken(
			Preconditions.checkNotNull(data.get("access_token")),
			Preconditions.checkNotNull(data.get("expires"))
		);
	}

	@Override
	public FacebookUserInfo userinfo(AccessToken token) {
		
		return ClientBuilder.newClient()
			.register(MoxyXmlFeature.class)
			.register(MoxyJsonFeature.class)
			.register(new LoggingFilter())
			.target(config.userinfoEndpoint)
			.queryParam("access_token", token.value())
			.queryParam("format", "json")
			.queryParam("method", "get")
			.queryParam("pretty", 0)
			.queryParam("suppress_http_code", 1)
			.request(MediaType.APPLICATION_JSON_TYPE)
			.get(FacebookUserInfo.class)
		;
	}

//	public static void main(String[] args) throws Exception {
//
//		OAuth2FacebookService google = new OAuth2FacebookService(
//
//			new Config(
//				"https://www.facebook.com/dialog/oauth",
//				"https://graph.facebook.com/oauth/access_token",
//				"https://graph.facebook.com/v2.1/me",
//				"",
//				"",
//				"https://bookagolf.com/en/oauth/facebook/callback"
//			)
//		);
//
//		PrintStream out = System.out;
//		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//
//		out.println("Facebook Authorization Flow");
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
//		AccessToken token = google.token(state, code);
//		out.println("Access Token:");
//		out.println(token);
//
//		UserInfo userinfo = google.userinfo(token);
//		out.println("User Info:");
//		out.println(userinfo);
//	}
}
