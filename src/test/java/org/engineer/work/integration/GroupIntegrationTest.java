package org.engineer.work.integration;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.integration.abstractlayer.AbstractIntegrationTest;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.bounding.UserGroups;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

/**
 * Integration test for group management feature.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupIntegrationTest extends AbstractIntegrationTest {

    private UserDTO userDTO;
    private GroupDTO groupDTO;

    @Before
    public void setUp() {
        userDTO = getCompleteUserDTO(MOCK_USERNAME);
        groupDTO = getCompleteGroupDTO(MOCK_GROUP_NAME);
    }

    @Test
    public void shouldCreateGroupAndAssignUserToIt() {
        getUserFacade().createUser(userDTO);
        getGroupFacade().createGroup(groupDTO);

        assertTrue(getUserFacade().getUserByUsername(MOCK_USERNAME) != null);
        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME) != null);
        assertTrue(getUserGroupsService().getUserGroupsByUsername(MOCK_USERNAME).getUsername().equals(userDTO.getUsername()));
        assertTrue(getUserGroupsService().getUserGroupsByUsername(MOCK_USERNAME).getGroups().get(0).equals(groupDTO.getName()));

        getGroupFacade().deleteGroup(MOCK_GROUP_NAME);
        getUserFacade().deleteUser(MOCK_USERNAME);

        assertTrue(getGroupService().getGroupByName(MOCK_GROUP_NAME) == null);
        assertTrue(getUserService().getUserByUsername(MOCK_USERNAME) == null);
        assertTrue(getUserGroupsService().getUserGroupsByUsername(MOCK_USERNAME) == null);
    }

    @Test
    public void shouldAssignUserToPendingGroup() {
        getUserFacade().createUser(userDTO);
        getGroupService().createGroup(groupDTO);

        final GroupEntity group = getGroupService().getGroupByName(MOCK_GROUP_NAME);
        group.getMembers().remove(0);
        getGroupService().updateGroup(group);

        final UserGroups userGroups = getUserGroupsService().getUserGroupsByUsername(MOCK_USERNAME);
        userGroups.getGroups().remove(0);
        getUserGroupsService().updateUserGroups(userGroups);

        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getMembers().isEmpty());
        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getPendingUsers().isEmpty());

        getGroupFacade().updatePendingUsers(MOCK_USERNAME, MOCK_GROUP_NAME, true);

        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getMembers().isEmpty());
        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getPendingUsers().get(0).equals(MOCK_USERNAME));

        getGroupFacade().updatePendingUsers(MOCK_USERNAME, MOCK_GROUP_NAME, false);

        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getMembers().isEmpty());
        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getPendingUsers().isEmpty());

        getGroupFacade().deleteGroup(MOCK_GROUP_NAME);
        getUserFacade().deleteUser(MOCK_USERNAME);

        assertTrue(getUserFacade().getUserByUsername(MOCK_USERNAME) == null);
        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME) == null);
        assertTrue(getUserGroupsService().getUserGroupsByUsername(MOCK_USERNAME) == null);
    }

//    @Test
//    public void shouldAddUserToGroupIfUserIsPending() {
//
//    }
}
