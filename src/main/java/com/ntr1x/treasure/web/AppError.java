package com.ntr1x.treasure.web;

public class AppError extends Error {

	private static final long serialVersionUID = 3935585716632477179L;

	public AppError() {
	}

	public AppError(String message) {
		super(message);
	}

	public AppError(Throwable cause) {
		super(cause);
	}

	public AppError(String message, Throwable cause) {
		super(message, cause);
	}
}
