package org.engineer.work.service;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.UserEntity;
import org.engineer.work.model.bounding.UserGroups;
import org.engineer.work.repository.GroupRepository;
import org.engineer.work.service.abstractlayer.AbstractUnitTest;
import org.engineer.work.service.impl.GroupServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.constraints.AssertTrue;

/**
 * Test of {@link GroupService} implementation.
 */
@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest extends AbstractUnitTest {

	@Mock
	private GroupRepository groupRepository;
	@Mock
	private UserService userService;
	@Mock
	private UserGroupsService userGroupsService;

	@InjectMocks
	private final GroupService groupService = new GroupServiceImpl();
	private GroupDTO testGroup;
	private UserDTO testUser;

	@Test
	public void shouldReturnGroupByName() {
		testGroup = getCompleteGroupDTO(MOCK_GROUP_NAME);

		Mockito.when(groupRepository.findOne(testGroup.getName())).thenReturn(new GroupEntity(testGroup));

		Assert.assertEquals(testGroup.getName(), groupService.getGroupByName(MOCK_GROUP_NAME).getName());
		Assert.assertEquals(testGroup.getDescription(), groupService.getGroupByName(MOCK_GROUP_NAME).getDescription());
		Assert.assertEquals(testGroup.getGroupOwner(), groupService.getGroupByName(MOCK_GROUP_NAME).getGroupOwner());
	}

	@Test
	public void shouldCreateGroup() {
		testGroup = getCompleteGroupDTO(MOCK_GROUP_NAME);

		Mockito.when(groupRepository.exists(MOCK_GROUP_NAME)).thenReturn(false);
		Mockito.when(userGroupsService.createOrUpdateUserGroups(testGroup.getGroupOwner(), testGroup.getName(), true)).thenReturn(true);

		Assert.assertTrue(groupService.createGroup(testGroup));
	}

	@Test
	public void shouldAddMemberToGroup() {
		testGroup = getCompleteGroupDTO(MOCK_GROUP_NAME);
		testUser = getCompleteUserDTO(MOCK_USERNAME);

		final GroupEntity groupEntity = new GroupEntity(testGroup);
		groupEntity.getPendingUsers().add(testUser.getUsername());

		Mockito.when(userService.getUserByUsername(testUser.getUsername())).thenReturn(new UserEntity(testUser));
		Mockito.when(groupRepository.findOne(testGroup.getName())).thenReturn(groupEntity);
		Mockito.when(userGroupsService.createOrUpdateUserGroups(testUser.getUsername(), testGroup.getName(), true)).thenReturn(true);
		Mockito.when(userGroupsService.getUserGroupsByUsername(testUser.getUsername())).thenReturn(new UserGroups(MOCK_USERNAME));

		Assert.assertTrue(groupService.updateGroupMembers(testUser.getUsername(), testGroup.getName(), true));
	}

	@Test
	public void shouldRemoveUserFromGroup() {
		testGroup = getCompleteGroupDTO(MOCK_GROUP_NAME);
		testUser = getCompleteUserDTO(MOCK_USERNAME);

		final GroupEntity groupEntity = new GroupEntity(testGroup);
		groupEntity.getMembers().add(testUser.getUsername());
		final UserGroups userGroups = new UserGroups(testUser.getUsername());
		userGroups.getGroups().add(testGroup.getName());

		Mockito.when(userService.getUserByUsername(testUser.getUsername())).thenReturn(new UserEntity(testUser));
		Mockito.when(groupRepository.findOne(testGroup.getName())).thenReturn(groupEntity);
		Mockito.when(userGroupsService.createOrUpdateUserGroups(testUser.getUsername(), testGroup.getName(), false)).thenReturn(true);
		Mockito.when(userGroupsService.getUserGroupsByUsername(testUser.getUsername())).thenReturn(userGroups);

		Assert.assertTrue(groupService.updateGroupMembers(testUser.getUsername(), testGroup.getName(), false));
	}
}
