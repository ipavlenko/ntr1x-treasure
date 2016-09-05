package com.ntr1x.treasure.web.oauth;

import java.io.StringReader;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface IOAuth2GoogleService extends IOAuth2Service {
	
	@Override
	GoogleRequestToken token();
	
	@Override
	GoogleAccessToken token(String code, String verifier);
	
	@Override
	GoogleUserInfo userinfo(AccessToken token);
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class GoogleRequestToken implements RequestToken {
		
		private String token;
		
		@Override
		public String value() { return token; };
		
		@Override
		public String secret() { return token; };
	}
	
	@Data
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	class GoogleAccessToken implements AccessToken {
		
		@XmlAttribute(name = "access_token")
		private String accessToken;
		
		@XmlAttribute(name = "id_token")
		@XmlJavaTypeAdapter(GoogleIdAdapter.class)
		private GoogleIdToken idToken;
		
		@XmlAttribute(name = "expires_in")
		private String expiresIn;
		
		@XmlAttribute(name = "token_type")
		private String tokenType;
		
		@XmlAnyAttribute
		private Map<QName,Object> any;
		
		@XmlAnyElement
		private Object[] others;
		
		@Override
		public String value() { return accessToken; }
		
		@Override
		public String secret() { return accessToken; };
		
		@Data
		@XmlRootElement
		@XmlAccessorType(XmlAccessType.FIELD)
		public static class GoogleIdToken {
			
			@XmlAttribute(name = "iss")
			private String iss;
			
			@XmlAttribute(name = "at_hash")
			private String atHash;
			
			@XmlAttribute(name = "email_verified")
			private boolean emailVerified;
			
			@XmlAttribute(name = "sub")
			private String sub;
			
			@XmlAttribute(name = "azp")
			private String azp;
			
			@XmlAttribute(name = "email")
			private String email;
			
			@XmlAttribute(name = "aud")
			private String aud;
			
			@XmlAttribute(name = "iat")
			private int iat;
			
			@XmlAttribute(name = "exp")
			private int exp;
		}
		
		public static class GoogleIdAdapter extends XmlAdapter<String, GoogleIdToken> {

			@Override
			public GoogleIdToken unmarshal(String v) {
			    
			    try {
    			    JAXBContext context = JAXBContext.newInstance(GoogleIdToken.class);
    			    Unmarshaller unmarshaler = context.createUnmarshaller();
    			    unmarshaler.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
    			    unmarshaler.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
    			    unmarshaler.setProperty(UnmarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
                    
    			    return unmarshaler.unmarshal(new StreamSource(new StringReader(v)), GoogleIdToken.class).getValue();
			    } catch (JAXBException e) {
			        throw new IllegalArgumentException(e);
			    }
//				return UtilFunctions.parseJson(JwtHelper.decode(v).getClaims(), GoogleIdToken.class);
			}

			@Override
			public String marshal(GoogleIdToken v) {
				throw new IllegalStateException("Not implemented");
			}
			
		}
	}
	
	@Data
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	class GoogleUserInfo implements UserInfo {
		
		@XmlAttribute(name = "kind")
		private String kind;
		
		@XmlAttribute(name = "gender")
		private String gender;
		
		@XmlAttribute(name = "sub")
		private String sub;
		
		@XmlAttribute(name = "name")
		private String name;
		
		@XmlAttribute(name = "given_name")
		private String givenName;
		
		@XmlAttribute(name = "family_name")
		private String familyName;
		
		@XmlAttribute(name = "profile")
		private String profile;
		
		@XmlAttribute(name = "picture")
		private String picture;
		
		@XmlAttribute(name = "email")
		private String email;
		
		@XmlAttribute(name = "email_verified")
		private boolean emailVerified;
		
		@XmlAttribute(name = "locale")
		private String locale;
		
		@XmlAttribute(name = "hd")
		private String hd;
		
		@XmlAnyAttribute
		private Map<QName,Object> any;
		
		@XmlAnyElement
		private Object[] others;

		@Override
		public String source() { return "google"; }
		
		@Override
		public String reference() { return sub; }

		@Override
		public String name() { return givenName; }
		
		@Override
		public String surname() { return familyName; }

		@Override
		public String email() { return email; }
	}
}
