package org.engineer.work.service.abstractlayer;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.enumeration.AuthorityRoles;

public abstract class AbstractUnitTest {

	/**
	 * Name constructed this way to avoid collision with real user name.
	 * User can not create account with more than 15 digits within username.
	 */
	protected static final String MOCK_USERNAME = "User123456789012";
	protected static final String MOCK_USER_PASSWORD = "test";
	/**
	 * Group name constructed this way to avoid collision with real group name.
	 * User can not create group with more than 20 digits within group name.
	 */
	protected static final String MOCK_GROUP_NAME = "Group1234567890123456";
	protected static final String MOCK_GROUP_DESCRIPTION = "Description";
	protected static final String MOCK_GROUP_OWNER = "Owner";
	protected static final String MOCK_FILES_LOCATION = "dropus.files.root";

	protected UserDTO getCompleteUserDTO(final String username) {
		final UserDTO userDTO = new UserDTO();

		userDTO.setUsername(username);
		userDTO.setPassword(MOCK_USER_PASSWORD);
		userDTO.setEnabled((byte) 0);
		userDTO.setRole(AuthorityRoles.USER);

		return userDTO;
	}

	protected GroupDTO getCompleteGroupDTO(final String groupName) {
		final GroupDTO groupDTO = new GroupDTO();

		groupDTO.setName(groupName);
		groupDTO.setDescription(MOCK_GROUP_DESCRIPTION);
		groupDTO.setGroupOwner(MOCK_GROUP_OWNER);

		return groupDTO;
	}
}
