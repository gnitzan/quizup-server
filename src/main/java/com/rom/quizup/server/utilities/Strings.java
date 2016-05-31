package com.rom.quizup.server.utilities;

/**
 * Static utility methods pertaining to {@code String} instances.
 */
public final class Strings {
	/**
	 * Prevent instantiating this class.
	 */
	private Strings() {
	}

	
	/**
	 * Returns {@code true} if the given string is null or empty.
	 *
	 * @param string
	 *          a string reference to check.
	 * @return {@code true} if the string is null or empty.
	 */
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}
}
