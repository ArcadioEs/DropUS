package org.engineer.work.integration;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.UserEntity;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for group management feature.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupIntegrationTest {

	private static final String USER_NAME = "User";
	private static final String GROUP_NAME = "Group";

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	private UserEntity userEntity;
	private GroupEntity groupEntity;

	@Before
	public void setUp() {
		final UserDTO userDTO = new UserDTO();
		userDTO.setUsername(USER_NAME);
		userDTO.setPassword("test");
		userDTO.setEnabled((byte) 1);
		userDTO.setRole(AuthorityRoles.USER);

		final GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName(GROUP_NAME);
		groupDTO.setGroupOwner(USER_NAME);

		userService.createUser(userDTO);
		groupService.createGroup(groupDTO);

		userEntity = userService.getUserByUsername(userDTO.getUsername());
		groupEntity = groupService.getGroupByName(groupDTO.getName());
	}

	@After
	public void cleanUp() {
		groupService.deleteGroup(GROUP_NAME);
		userService.deleteUser(USER_NAME);
	}

	@Test
	public void shouldAssignGroupToUser() {
		final GroupEntity userGroupToCheck = userService.getUserByUsername(USER_NAME).getGroups().get(0);

		Assert.assertEquals(userGroupToCheck.getName(), groupEntity.getName());
		Assert.assertEquals(userGroupToCheck.getGroupOwner(), groupEntity.getGroupOwner());
	}
}
