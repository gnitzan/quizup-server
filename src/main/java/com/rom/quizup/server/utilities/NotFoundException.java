package com.rom.quizup.server.utilities;

/**
 * An @see {@link Exception} that is thrown when a database record
 * is not found.
 * 
 * @author rom
 *
 */
public class NotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public NotFoundException() {
		super();
	}

	public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}
}