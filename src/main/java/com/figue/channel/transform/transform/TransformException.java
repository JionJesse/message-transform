package com.figue.channel.transform.transform;

import com.figue.channel.transform.exception.AccessException;

public class TransformException extends AccessException {

	private static final long serialVersionUID = 1L;

	public TransformException() {
		super();
	}

	public TransformException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransformException(String message) {
		super(message);
	}

	public TransformException(Throwable cause) {
		super(cause);
	}

}
