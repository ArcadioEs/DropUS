package org.engineer.work.exception.user;

import java.text.MessageFormat;

/**
 * Custom exception.
 */
public class UserNotFoundException extends Exception {

	public UserNotFoundException(final String username) {
		super(MessageFormat.format("User with username {0} could not be found", username));
	}
}
