package com.rom.quizup.server.utilities;

/**
 * An @see {@link Exception} that represents unauthorized access to the server.
 * 
 * @author rom
 *
 */
public class UnauthorizedException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
	}

	public UnauthorizedException(String message) {
		super(message);
	}

	public UnauthorizedException(Throwable cause) {
		super(cause);
	}

	public UnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}