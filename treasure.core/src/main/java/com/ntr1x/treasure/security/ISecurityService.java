package com.ntr1x.treasure.security;

import lombok.Data;

public interface ISecurityService {

	int randomInt();
	String hashPassword(String password);
	
	byte[] encrypt(byte[] bytes);
	byte[] decrypt(byte[] bytes);
	
	byte[] toByteArray(SecurityToken token);
	String toString(SecurityToken token);
	SecurityToken parseToken(byte[] bytes);
	SecurityToken parseToken(String token);
	
	byte[] toByteArray(SecuritySession session);
	String toString(SecuritySession session);
	SecuritySession parseSession(byte[] bytes);
	SecuritySession parseSession(String session);
	
	@Data
	class SecurityToken {
		
		public final long id;
		public final int event;
		public final int signature;
	}
	
	@Data
	class SecuritySession {
		
		public final long id;
		public final int signature;
	}
}
