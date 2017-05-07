package org.engineer.work.exception.group;

import java.text.MessageFormat;

/**
 * Custom exception.
 */
public class GroupNotFoundException extends Exception {

	public GroupNotFoundException(final String name) {
		super(MessageFormat.format("Group {0} could not be found", name));
	}
}
