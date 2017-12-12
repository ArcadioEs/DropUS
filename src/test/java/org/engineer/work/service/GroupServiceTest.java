package org.engineer.work.service;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.repository.GroupRepository;
import org.engineer.work.service.impl.GroupServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test of {@link GroupService} implementation.
 */
@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

	/**
	 * Group name constructed this way to avoid collision with real group name.
	 * User can not create group with more than 20 digits within group name.
	 */
	private static final String MOCK_GROUP_NAME = "Group1234567890123456";
	private static final String MOCK_GROUP_DESCRIPTION = "Description";
	private static final String MOCK_GROUP_OWNER = "Owner";

	@Mock
	private GroupRepository groupRepository;

	@InjectMocks
	private final GroupService userService = new GroupServiceImpl();
	private GroupDTO testGroup;

	@Test
	public void shouldReturnGroupByName() {
		testGroup = getCompleteGroupDTO(MOCK_GROUP_NAME);

		Mockito.when(groupRepository.findOne(MOCK_GROUP_NAME)).thenReturn(new GroupEntity(testGroup));

		Assert.assertEquals(testGroup.getName(), userService.getGroupByName(MOCK_GROUP_NAME).getName());
		Assert.assertEquals(testGroup.getDescription(), userService.getGroupByName(MOCK_GROUP_NAME).getDescription());
		Assert.assertEquals(testGroup.getGroupOwner(), userService.getGroupByName(MOCK_GROUP_NAME).getGroupOwner());
	}

	private GroupDTO getCompleteGroupDTO(final String groupName) {
		final GroupDTO groupDTO = new GroupDTO();

		groupDTO.setName(groupName);
		groupDTO.setDescription(MOCK_GROUP_DESCRIPTION);
		groupDTO.setGroupOwner(MOCK_GROUP_OWNER);

		return groupDTO;
	}
}
