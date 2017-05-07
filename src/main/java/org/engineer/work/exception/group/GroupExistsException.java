package org.engineer.work.exception.group;

import java.text.MessageFormat;

/**
 * Custom exception.
 */
public class GroupExistsException extends Exception {

	public GroupExistsException(final String name) {
		super(MessageFormat.format("Group {0} already exists", name));
	}
}
