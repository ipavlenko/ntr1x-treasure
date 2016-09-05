package com.ntr1x.treasure.web;

public class AppException extends Exception {

	private static final long serialVersionUID = -5966303473948913660L;

	public AppException() {
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(Throwable cause) {
		super(cause);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}
}
