package com.ntr1x.treasure.web.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class HashUtils {
	
	private static final String ALGORITHM_MD5 = "MD5";
	private static final String ALGORITHM_SHA1 = "SHA-1";

	/**
	 * Gets a hash from the specified text in specified charset
	 * @param text The text
	 * @param charsetName The charset name
	 * @return The hash
	 */
	public String hash(String text, String charsetName) {
		return hash(text, Charset.forName(charsetName));
	}

	/**
	 * Gets a hash from the specified data
	 * @param data The data
	 * @return The hash
	 */
	public abstract byte[] hash(byte[] data);

	/**
	 * Gets a hash in HEX from the specified text in specified charset
	 * @param text The text
	 * @param charset The character set
	 * @return The hash in HEX
	 */
	public abstract String hash(String text, Charset charset);

	/**
	 * Use java MessageDigest to create hash
	 * @param algorithmIdentifier hash algorithm identifier for MessageDigest
	 * @param data data to hash
	 * @return hash bytes
	 */
	private static byte[] useJavaHashAlgorithm(String algorithmIdentifier, byte[] data) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithmIdentifier);
			md.update(data, 0, data.length);
			byte[] hash = md.digest();
			return hash;
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * The MD5 hash utils
	 */
	public static final HashUtils MD5 = new HashUtils() {

		@Override
		public byte[] hash(byte[] data) {
			return useJavaHashAlgorithm(ALGORITHM_MD5, data);
		}

		@Override
		public String hash(String text, Charset charset) {
			return ConversionUtils.HEX.encode(hash(text.getBytes(charset)));
		}
	};

	/**
	 * The SHA-1 hash utils
	 */
	public static final HashUtils SHA1 = new HashUtils() {

		@Override
		public byte[] hash(byte[] data) {
			return useJavaHashAlgorithm(ALGORITHM_SHA1, data);
		}

		@Override
		public String hash(String text, Charset charset) {
			return ConversionUtils.HEX.encode(hash(text.getBytes(charset)));
		}
	};
}