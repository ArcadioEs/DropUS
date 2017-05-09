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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * Integration test for group management feature.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupIntegrationTest {

	private static final Logger LOG = LoggerFactory.getLogger(GroupIntegrationTest.class);

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	private UserEntity userEntity;
	private GroupEntity groupEntity;

	@Before
	public void setUp() {
		final UserDTO userDTO = new UserDTO();
		userDTO.setUsername("User");
		userDTO.setPassword("test");
		userDTO.setEnabled((byte) 1);
		userDTO.setRole(AuthorityRoles.USER);

		final GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("Group");
		groupDTO.setGroupOwner("User");

		userService.createUser(userDTO);
		groupService.createGroup(groupDTO);

		userEntity = userService.getUserByUsername(userDTO.getUsername());
		groupEntity = groupService.getGroupByName(groupDTO.getName());
	}

	@After
	public void cleanUp() {
		groupService.deleteGroup(groupEntity.getName());
		userService.deleteUser(userEntity.getUsername());
	}

	@Test
	public void shouldAssignGroupToUser() {
		groupService.updateGroupMembers(userEntity.getUsername(), groupEntity.getName());

		final GroupEntity userGroupToCheck = userService.getUserByUsername(userEntity.getUsername()).getGroups().get(0);

		Assert.assertEquals(userGroupToCheck.getName(), groupEntity.getName());
		Assert.assertEquals(userGroupToCheck.getGroupOwner(), groupEntity.getGroupOwner());
	}
}
