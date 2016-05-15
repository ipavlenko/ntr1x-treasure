package com.ntr1x.treasure.security;

import java.nio.ByteBuffer;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;

import com.ntr1x.treasure.utils.ConversionUtils;
import com.ntr1x.treasure.utils.CryptoUtils;
import com.ntr1x.treasure.utils.HashUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApplicationScoped
public class SecurityService implements ISecurityService {
	
	@Resource(lookup = "java:app/config/SecurityService")
	private Config config;
	
	private final Random random = new Random();
	
	private byte[] key;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Config {
		
		public String prefix;
		public String suffix;
		public String key;
	}
	
	@PostConstruct
	private void init() {
		key = ConversionUtils.BASE64.decode(config.key);
	}
	
	@Override
	public String hashPassword(String password) {
		
		if (password == null) return null;
		
		return HashUtils.MD5.hash(String.format(
			"%s/%s/%s",
			config.prefix,
			password,
			config.suffix
		), "UTF-8");
	}

	@Override
	public int randomInt() {
		return random.nextInt();
	}
	
	@Override
	public byte[] encrypt(byte[] bytes) {
		return CryptoUtils.AES128.encrypt(key, bytes);
	}

	@Override
	public byte[] decrypt(byte[] bytes) {
		return CryptoUtils.AES128.decrypt(key, bytes);
	}
	
	@Override
	public byte[] toByteArray(SecurityToken token) {
		
		ByteBuffer buffer = ByteBuffer.allocate(17)
			.putLong(token.id)
			.putInt(token.event)
			.putInt(token.signature)
		;
		
		buffer.rewind();
		byte[] bytes = buffer.array();
		
		byte crc2 = (byte) 1011;
		for (int i = 0; i < 16; i++) {
			crc2 |= bytes[i];
		}
		
		buffer.position(bytes.length - 1);
		buffer.put(crc2);
		return buffer.array();
	}
	
	@Override
	public String toString(SecurityToken token) {
		return ConversionUtils.BASE62.encode(encrypt(toByteArray(token)));
	}
	
	@Override
	public SecurityToken parseToken(byte[] bytes) {
		
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.rewind();
		
		long id = buffer.getLong();
		int event = buffer.getInt();
		int signature = buffer.getInt();
		byte crc = buffer.get();
		
		byte crc2 = (byte) 1011;
		for (int i = 0; i < 16; i++) {
			crc2 |= bytes[i];
		}
		
		if (crc2 != crc) throw new IllegalArgumentException();
			
		return new SecurityToken(
			id,
			event,
			signature
		);
	}
	
	@Override
	public SecurityToken parseToken(String token) {
		return parseToken(decrypt(ConversionUtils.BASE62.decode(token)));
	}
	
	@Override
	public byte[] toByteArray(SecuritySession session) {
		
		ByteBuffer buffer = ByteBuffer.allocate(13)
			.putLong(session.id)
			.putInt(session.signature)
		;
		
		buffer.rewind();
		byte[] bytes = buffer.array();
		
		byte crc2 = (byte) 1011;
		for (int i = 0; i < 12; i++) {
			crc2 |= bytes[i];
		}
		
		buffer.position(bytes.length - 1);
		buffer.put(crc2);
		return buffer.array();
	}
	
	public String toString(SecuritySession session) {
		return ConversionUtils.BASE62.encode(encrypt(toByteArray(session)));
	}

	@Override
	public SecuritySession parseSession(byte[] bytes) {
		
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.rewind();
		
		long id = buffer.getLong();
		int sign = buffer.getInt();
		byte crc = buffer.get();
		
		byte crc2 = (byte) 1011;
		for (int i = 0; i < 12; i++) {
			crc2 |= bytes[i];
		}
		
		if (crc2 != crc) throw new IllegalArgumentException();
			
		return new SecuritySession(
			id,
			sign
		);
	}
	
	@Override
	public SecuritySession parseSession(String session) {
		return parseSession(decrypt(ConversionUtils.BASE62.decode(session)));
	}
}
