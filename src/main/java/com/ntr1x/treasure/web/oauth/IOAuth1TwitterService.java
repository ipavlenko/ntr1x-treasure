package com.ntr1x.treasure.web.oauth;

import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface IOAuth1TwitterService extends IOAuth1Service {
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class TwitterRequestToken implements RequestToken {
		
		public String oauthToken;
		public String oauthTokenSecret;
		public String oauthCallbackConfirmed;
		
		@Override
		public String value() { return oauthToken; }
		
		@Override
		public String secret() { return oauthTokenSecret; }
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class TwitterAccessToken implements AccessToken {
		
		public String oauthToken;
		public String oauthTokenSecret;
		
		@Override
		public String value() { return oauthToken; }
		
		@Override
		public String secret() { return oauthTokenSecret; }
	}
	
	@XmlRootElement
	@NoArgsConstructor
	@AllArgsConstructor
	class TwitterUserInfo implements UserInfo {
		
		@XmlAttribute(name = "id_str")
		private String idStr;
		
		@XmlAttribute(name = "name")
		private String name;
		
		@XmlAttribute(name = "screen_name")
		private String screenName;
		
		@XmlAnyAttribute
		private Map<QName,Object> any;
		
		@XmlAnyElement
		private Object[] others;

		@Override
		public String source() { return "twitter"; }

		@Override
		public String reference() { return idStr; }

		@Override
		public String name() {
			
			String[] data = name.split(" ");
			return data.length > 0 ? data[0] : "";
		}

		@Override
		public String surname() {
			
			String[] data = name.split(" ");
			return data.length > 1 ? data[1] : "";
		}
		
		@Override
		public String email() { return ""; }
	}
}
