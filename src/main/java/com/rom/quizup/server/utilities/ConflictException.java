package com.rom.quizup.server.utilities;

import com.rom.quizup.server.models.Invitation;

/**
 * An @see {@link Exception} that represents an @see {@link Invitation}
 * which its state has changed and the @see {@link Invitation} can not
 * be accepted.
 * 
 * @author rom
 *
 */
public class ConflictException extends Exception {
	private static final long serialVersionUID = 1L;

	public ConflictException() {
	}

	public ConflictException(String message) {
		super(message);
	}

	public ConflictException(Throwable cause) {
		super(cause);
	}

	public ConflictException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
