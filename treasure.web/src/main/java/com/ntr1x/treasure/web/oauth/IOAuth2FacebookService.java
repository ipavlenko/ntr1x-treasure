package com.ntr1x.treasure.web.oauth;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface IOAuth2FacebookService extends IOAuth2Service {
	
	@Override
	FacebookRequestToken token();
	
	@Override
	FacebookAccessToken token(String token, String verifier);
	
	@Override
	FacebookUserInfo userinfo(AccessToken token);
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class FacebookRequestToken implements RequestToken {
		
		private String token;
		
		@Override
		public String value() { return token; };
		
		@Override
		public String secret() { return token; }
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class FacebookAccessToken implements AccessToken {
		
		private String accessToken;
		private String expires;
		
		@Override
		public String value() { return accessToken; }
		
		@Override
		public String secret() { return accessToken; }
	}
	
	@Data
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	class FacebookUserInfo implements UserInfo {
		
		@XmlAttribute(name = "id")
		private String id;
		
		@XmlAttribute(name = "email")
		private String email;
		
		@XmlAttribute(name = "first_name")
		private String firstName;
		
		@XmlAttribute(name = "last_name")
		private String lastName;
		
		@XmlAttribute(name = "gender")
		private String gender;
		
		@XmlAttribute(name = "link")
		private String link;
		
		@XmlAttribute(name = "locale")
		private String locale;
		
		@XmlAttribute(name = "name")
		private String name;
		
		@XmlAttribute(name = "timezone")
		private int timezone;
		
		@XmlAttribute(name = "updated_time")
		private String updatedTime;
		
		@XmlAttribute(name = "verified")
		private boolean verified;
		
		@XmlAnyAttribute
		private Map<QName,Object> any;
		
		@XmlAnyElement
		private Object[] others;

		@Override
		public String source() { return "facebook"; }
		
		@Override
		public String reference() { return id; }

		@Override
		public String name() { return firstName; }
		
		@Override
		public String surname() { return lastName; }

		@Override
		public String email() { return email; }
	}
}
