package com.ntr1x.treasure.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class CryptoUtils {

	private static final String MESSAGE_CANNOT_CREATE_CIPHER_OBJECT = "Catton create Cipher object for algorithm: {0}";
	private static final String MESSAGE_INVALID_KEY = "Invalid key for algorithm: {0}";
	private static final String MESSAGE_ENCRYPTION_FAILED = "Encryption failed for algorithm: {0}";
	private static final String MESSAGE_DECRYPTION_FAILED = "Decryption failed for algorithm: {0}";

	public static class AESCryptoUtils extends CryptoUtils {

		private static final String NAME = "AES"; //$NON-NLS-1$

		private int m_length;

		public AESCryptoUtils(int length) {
			m_length = length;
		}

		@Override
		public byte[] encrypt(byte[] key, byte[] data) throws IllegalArgumentException {
			try {
				SecretKeySpec skeySpec = new SecretKeySpec(key, NAME);
				Cipher cipher = Cipher.getInstance(NAME);
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
				return cipher.doFinal(data);
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_CANNOT_CREATE_CIPHER_OBJECT, NAME), e);
			} catch (NoSuchPaddingException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_CANNOT_CREATE_CIPHER_OBJECT, NAME), e);
			} catch (InvalidKeyException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_INVALID_KEY, NAME), e);
			} catch (IllegalBlockSizeException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_ENCRYPTION_FAILED, NAME), e);
			} catch (BadPaddingException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_ENCRYPTION_FAILED, NAME), e);
			}
		}

		@Override
		public byte[] decrypt(byte[] key, byte[] encrypted) throws IllegalArgumentException {
			try {
				SecretKeySpec skeySpec = new SecretKeySpec(key, NAME);
				Cipher cipher = Cipher.getInstance(NAME);
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
				return cipher.doFinal(encrypted);
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_CANNOT_CREATE_CIPHER_OBJECT, NAME), e);
			} catch (NoSuchPaddingException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_CANNOT_CREATE_CIPHER_OBJECT, NAME), e);
			} catch (InvalidKeyException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_INVALID_KEY, NAME), e);
			} catch (IllegalBlockSizeException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_DECRYPTION_FAILED, NAME), e);
			} catch (BadPaddingException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_DECRYPTION_FAILED, NAME), e);
			}
		}

		@Override
		public byte[] generateKey() throws IllegalArgumentException {
			try {
				KeyGenerator kgen = KeyGenerator.getInstance(NAME);
				kgen.init(m_length);
				SecretKey skey = kgen.generateKey();
				return skey.getEncoded();
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalArgumentException(MessageFormat.format(MESSAGE_CANNOT_CREATE_CIPHER_OBJECT, NAME), e);
			}
		}
	}

	public static CryptoUtils AES128 = new AESCryptoUtils(128);
	public static CryptoUtils AES256 = new AESCryptoUtils(256);
	
	public static CryptoUtils XOR = new CryptoUtils() {
		
		@Override
		public byte[] encrypt(byte[] key, byte[] data) throws IllegalArgumentException {
			byte[] res = new byte[data.length];
			for (int i = 0; i < data.length; i++) {
				int shiftId = i % key.length;
				res[i] = (byte) (data[i] ^ key[shiftId]);
			}
			return res;
		}

		@Override
		public byte[] decrypt(byte[] key, byte[] encrypted) throws IllegalArgumentException {
			byte[] res = new byte[encrypted.length];
			for (int i = 0; i < encrypted.length; i++) {
				int shiftId = i % key.length;
				res[i] = (byte) (encrypted[i] ^ key[shiftId]);
			}
			return res;
		}

		@Override
		public byte[] generateKey() throws IllegalArgumentException {
			Random rand = new Random();
			int value = rand.nextInt();
			return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
		}
	};

	/**
	 * Encrypts data using specified key
	 * @param key Encryption/decryption key
	 * @param data Data to encrypt
	 * @return Encrypted data
	 * @throws IllegalArgumentException; If cannot encrypt
	 */
	public abstract byte[] encrypt(byte[] key, byte[] data) throws IllegalArgumentException;

	/**
	 * Decrypts encrypted data using specified key
	 * @param key Encryption/decryption key
	 * @param encrypted Encrypted data
	 * @return Data
	 * @throws IllegalArgumentException; If cannot decrypt
	 */
	public abstract byte[] decrypt(byte[] key, byte[] encrypted) throws IllegalArgumentException;

	/**
	 * Generates encryption key
	 * @return Generated key
	 * @throws IllegalArgumentException; If cannot generate key
	 */
	public abstract byte[] generateKey() throws IllegalArgumentException;

	/**
	 * Gets int value as byte array.
	 * @param value The integer value.
	 * @return Bytes of the value.
	 */
	public static byte[] toByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
	}

	/**
	 * Gets long value as byte array.
	 * @param value The long integer value.
	 * @return Bytes of the value.
	 */
	public static byte[] toByteArray(long value) {
		return new byte[] { (byte) (value >>> 56), (byte) (value >>> 48), (byte) (value >>> 40), (byte) (value >>> 32), (byte) (value >>> 24),
				(byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
	}

	public static long longFromByteArray(byte[] bytes) {
		long value = 0;
		for (int i = 0; i < bytes.length; i++) {
			value = (value << 8) + (bytes[i] & 0xFF);
		}
		return value;
	}

	/**
	 * Gets int value of the specified bytes.
	 * @param bytes The bytes.
	 * @return The integer value.
	 */
	public static int fromByteArray(byte[] bytes) {
		return (bytes[0] << 24) + ((bytes[1] & 0xFF) << 16) + ((bytes[2] & 0xFF) << 8) + (bytes[3] & 0xFF);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(ConversionUtils.BASE64.encode(AES128.generateKey()));
	}
}