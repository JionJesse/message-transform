package com.figue.channel.transform.exception;

public class AccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccessException() {
		super();
	}

	public AccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessException(String message) {
		super(message);
	}

	public AccessException(Throwable cause) {
		super(cause);
	}

}
