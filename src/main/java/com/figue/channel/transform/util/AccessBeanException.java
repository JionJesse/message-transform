package com.figue.channel.transform.util;

import com.figue.channel.transform.exception.AccessException;

public class AccessBeanException extends AccessException {
	private static final long serialVersionUID = 1L;

	public AccessBeanException() {
		super();
	}

	public AccessBeanException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessBeanException(String message) {
		super(message);
	}

	public AccessBeanException(Throwable cause) {
		super(cause);
	}

}
