package com.figue.channel.transform.validate;

import com.figue.channel.transform.exception.AccessException;

public class ValidateException extends AccessException {

	private static final long serialVersionUID = 1L;

	public ValidateException() {
		super();
	}

	public ValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidateException(String message) {
		super(message);
	}

	public ValidateException(Throwable cause) {
		super(cause);
	}

}
