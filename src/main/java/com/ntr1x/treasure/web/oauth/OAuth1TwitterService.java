package com.ntr1x.treasure.web.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.ntr1x.treasure.web.utils.ConversionUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuth1TwitterService implements IOAuth1TwitterService {
	
	private final OAuth1TwitterFactory.Config config;
	private final Credentials credentials;
	
	@Override
	public URI auth(String token) {
		
		return ClientBuilder.newClient()
			.target(config.authorizationEndpoint)
			.getUriBuilder()
				.queryParam("oauth_token", token)
				.build()
		;
	}
	
	@Override
	public TwitterRequestToken token() {
		
		String body = ClientBuilder.newClient()
	        .register(LoggingFeature.class)
			.target(config.requestTokenEndpoint)
			.request(MediaType.TEXT_PLAIN_TYPE)
			.header("Authorization", sign("POST", config.requestTokenEndpoint, null, null, "oauth_callback", encode(credentials.redirectUri)))
			.post(null, String.class)
		;
		
		Map<String, String> data = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(body);
		
		return new TwitterRequestToken(
			Preconditions.checkNotNull(data.get("oauth_token")),
			Preconditions.checkNotNull(data.get("oauth_token_secret")),
			Preconditions.checkNotNull(data.get("oauth_callback_confirmed"))
		);
	}

	@Override
	public TwitterAccessToken token(String token, String verifier) {
		
		String body = ClientBuilder.newClient()
	        .register(LoggingFeature.class)
			.target(config.accessTokenEndpoint)
			.request(MediaType.WILDCARD_TYPE)
			.header("Authorization", sign("POST", config.accessTokenEndpoint, token, token, "oauth_callback", encode(credentials.redirectUri)))
			.post(
				Entity.entity(
					String.format("%s=%s", "oauth_verifier", verifier),
					MediaType.APPLICATION_FORM_URLENCODED_TYPE
				),
				String.class
			)
		;
		
		Map<String, String> data = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(body);
		
		return new TwitterAccessToken(
			Preconditions.checkNotNull(data.get("oauth_token")),
			Preconditions.checkNotNull(data.get("oauth_token_secret"))
		);
	}

	@Override
	public TwitterUserInfo userinfo(AccessToken token) {
		
		return ClientBuilder.newClient()
			.register(MoxyXmlFeature.class)
			.register(MoxyJsonFeature.class)
			.register(LoggingFeature.class)
			.target(config.userinfoEndpoint)
			.request(MediaType.APPLICATION_JSON_TYPE)
			.header("Authorization", sign("GET", config.userinfoEndpoint, token.value(), token.secret()))
			.get(TwitterUserInfo.class)
		;

		/*
		UtilFunctions.parseJson(
			ClientBuilder.newClient()
				.register(MoxyXmlFeature.class)
				.register(MoxyJsonFeature.class)
				.register(new LoggingFilter())
				.target(config.userinfoEndpoint)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("Authorization", sign("GET", config.userinfoEndpoint, token.value(), token.secret()))
				.get(String.class),
			TwitterUserInfo.class
		);
		*/
	}
	
	private String sign(String method, String url, String token, String secret, String... params) {
		
		Set<String[]> p = new TreeSet<>((e1, e2) -> {
				
			int res = e1[0].compareTo(e2[0]);
			if (res == 0) {
				res = e1[1].compareTo(e2[1]);
			}
			return res;
		});
		
		p.add(new String[] { "oauth_consumer_key", credentials.clientId });
		p.add(new String[] { "oauth_nonce",  UUID.randomUUID().toString().replace("-", "") });
		p.add(new String[] { "oauth_signature_method", "HMAC-SHA1" });
		p.add(new String[] { "oauth_timestamp", "" + new Date().getTime() / 1000 });
		p.add(new String[] { "oauth_version", "1.0" });
		
		if (token != null) {
			p.add(new String[] { "oauth_token", token });
		}
		
		for (int i = 0; i < params.length / 2; i += 2) {
			p.add(new String[] { params[i], params[i + 1] });
		}
		
		String signature = null; {
			
			StringBuilder builder = new StringBuilder(); {
				int i = 0;
				for (String[] e : p) {
					if (i++ > 0) builder.append('&');
					builder.append(String.format("%s=%s", e[0], e[1]));
				}
			}
			
			String baseString = String.format(
				"%s&%s&%s",
				method,
				encode(url),
				encode(builder.toString())
			);
			
			String keyString = String.format("%s&%s", credentials.clientSecret, token != null ? encode(secret) : "");
			
			try {
				
		        Mac mac = Mac.getInstance("HmacSHA1");
		        mac.init(new SecretKeySpec(keyString.getBytes(), "HmacSHA1"));
		        byte[] text = baseString.getBytes();
		        signature = encode(new String(ConversionUtils.BASE64.encode(mac.doFinal(text))).trim());
		        
			} catch (Exception e) {
				
				throw new IllegalStateException(e);
			}
		}
		
		p.add(new String[] { "oauth_signature", signature });
		
		StringBuilder builder = new StringBuilder(); {
			int i = 0;
			for (String[] e : p) {
				if (i++ > 0) builder.append(", ");
				builder.append(String.format("%s=\"%s\"", e[0], e[1]));
			}
		}
			
		return String.format("OAuth %s", builder.toString());
	}
	
	private String encode(String string) {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

//	public static void main(String[] args) throws Exception {
//
//		OAuth1TwitterService twitter = new OAuth1TwitterService(
//			new Config(
//				"https://api.twitter.com/oauth/authenticate",
//				"https://api.twitter.com/oauth/request_token",
//				"https://api.twitter.com/oauth/access_token",
//				"https://api.twitter.com/1.1/account/verify_credentials.json",
//				"",
//				"",
//				"https://bookagolf.com/en/oauth/twitter/callback"
//			)
//		);
//
//		PrintStream out = System.out;
//		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//
//		out.println("Twitter Authorization Flow");
//
//		RequestToken requestToken = twitter.token();
//
//		out.println("Request Token:");
//		System.out.println(requestToken);
//
//		URI uri = twitter.auth(requestToken.value());
//
//		out.println("Authorization URL:");
//		out.println(uri.toString());
//
//		out.println("Input Verifier:");
//		String verifier = in.readLine();
//
//		TwitterAccessToken token = twitter.token(requestToken.value(), verifier);
//		out.println("Access Token:");
//		out.println(token);
//
//		TwitterUserInfo userinfo = twitter.userinfo(token);
//		out.println("User Info:");
//		out.println(userinfo);
//	}
}
