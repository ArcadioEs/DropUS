package org.engineer.work.abstractlayer;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.PostDTO;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.enumeration.AuthorityRoles;

import java.util.Calendar;

public abstract class AbstractTest {

	/**
	 * Name constructed this way to avoid collision with real user name.
	 * User can not create account with special characters in it (_).
	 */
	protected static final String MOCK_USERNAME = "Test_user";
	protected static final String OTHER_TEST_USER = "Other_test_user";
	protected static final String MOCK_USER_PASSWORD = "test";
	/**
	 * Group name constructed this way to avoid collision with real group name.
	 * User can not create group with special characters in it (_).
	 */
	protected static final String MOCK_GROUP_NAME = "Test_group";
	protected static final String MOCK_GROUP_DESCRIPTION = "Description";
	protected static final String MOCK_FILES_LOCATION = "dropus.files.root";

	protected static final String MOCK_GROUP_CONTENT = "Test_content";

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
		groupDTO.setGroupOwner(MOCK_USERNAME);

		return groupDTO;
	}

	protected PostDTO getCompletePostDTO(final String mockAuthor, final String groupName) {
		final PostDTO postDTO = new PostDTO();

		postDTO.setAuthor(mockAuthor);
		postDTO.setPostGroup(groupName);
		postDTO.setPostContent(MOCK_GROUP_CONTENT);

		return postDTO;
	}
}
