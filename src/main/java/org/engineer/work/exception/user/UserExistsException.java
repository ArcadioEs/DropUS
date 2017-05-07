package org.engineer.work.exception.user;

import java.text.MessageFormat;

/**
 * Custom exception.
 */
public class UserExistsException extends Exception {

	public UserExistsException(final String username) {
		super(MessageFormat.format("Username {0} already in use", username));
	}
}
