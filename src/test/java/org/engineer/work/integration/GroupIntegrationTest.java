package org.engineer.work.integration;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.facade.GroupFacade;
import org.engineer.work.facade.UserFacade;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.UserGroups;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.UserGroupsService;
import org.engineer.work.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertTrue;

/**
 * Integration test for group management feature.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupIntegrationTest {

    private static final String USER_NAME = "User";
    private static final String GROUP_NAME = "Group";
    private static final String DESCRIPTION = "Description";
    private static final String PASSWORD = "Password";

    @Resource
    private UserService userService;
    @Resource
    private UserFacade userFacade;
    @Resource
    private GroupService groupService;
    @Resource
    private GroupFacade groupFacade;
    @Resource
    private UserGroupsService userGroupsService;

    private UserDTO userDTO;
    private GroupDTO groupDTO;

    @Before
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername(USER_NAME);
        userDTO.setPassword(PASSWORD);
        userDTO.setEnabled((byte) 1);
        userDTO.setRole(AuthorityRoles.USER);

        groupDTO = new GroupDTO();
        groupDTO.setName(GROUP_NAME);
        groupDTO.setGroupOwner(USER_NAME);
        groupDTO.setDescription(DESCRIPTION);
    }

    @Test
    public void shouldCreateGroupAndAssignUserToIt() {
        userFacade.createUser(userDTO);
        groupFacade.createGroup(groupDTO);

        assertTrue(userFacade.getUserByUsername(USER_NAME) != null);
        assertTrue(groupFacade.getGroupByName(GROUP_NAME) != null);
        assertTrue(userGroupsService.getUserGroupsByUsername(USER_NAME).getUsername().equals(userDTO.getUsername()));
        assertTrue(userGroupsService.getUserGroupsByUsername(USER_NAME).getGroups().get(0).equals(groupDTO.getName()));

        groupFacade.deleteGroup(GROUP_NAME);
        userFacade.deleteUser(USER_NAME);

        assertTrue(groupService.getGroupByName(GROUP_NAME) == null);
        assertTrue(userService.getUserByUsername(USER_NAME) == null);
        assertTrue(userGroupsService.getUserGroupsByUsername(USER_NAME) == null);
    }

    @Test
    public void shouldAssignUserToPendingGroup() {
        userFacade.createUser(userDTO);
        groupService.createGroup(groupDTO);

        final GroupEntity group = groupService.getGroupByName(GROUP_NAME);
        group.getMembers().remove(0);
        groupService.updateGroup(group);

        final UserGroups userGroups = userGroupsService.getUserGroupsByUsername(USER_NAME);
        userGroups.getGroups().remove(0);
        userGroupsService.updateUserGroups(userGroups);

        assertTrue(groupFacade.getGroupByName(GROUP_NAME).getMembers().isEmpty());
        assertTrue(groupFacade.getGroupByName(GROUP_NAME).getPendingUsers().isEmpty());

        groupFacade.updatePendingUsers(USER_NAME, GROUP_NAME, true);

        assertTrue(groupFacade.getGroupByName(GROUP_NAME).getMembers().isEmpty());
        assertTrue(groupFacade.getGroupByName(GROUP_NAME).getPendingUsers().get(0).equals(USER_NAME));

        groupFacade.updatePendingUsers(USER_NAME, GROUP_NAME, false);

        assertTrue(groupFacade.getGroupByName(GROUP_NAME).getMembers().isEmpty());
        assertTrue(groupFacade.getGroupByName(GROUP_NAME).getPendingUsers().isEmpty());

        groupFacade.deleteGroup(GROUP_NAME);
        userFacade.deleteUser(USER_NAME);

        assertTrue(userFacade.getUserByUsername(USER_NAME) == null);
        assertTrue(groupFacade.getGroupByName(GROUP_NAME) == null);
        assertTrue(userGroupsService.getUserGroupsByUsername(USER_NAME) == null);
    }

    @Test
    public void shouldAddUserToGroupIfUserIsPending() {
//        userService.createUser(userDTO);
//        groupService.createGroup(groupDTO);
//
//        final GroupEntity group = groupService.getGroupByName(GROUP_NAME);
//        group.getMembers().remove(0);
//        groupService.updateGroup(group);
//
//        final UserGroups userGroups = userGroupsService.getUserGroupsByUsername(USER_NAME);
//        userGroups.getGroups().remove(0);
//        userGroupsService.updateUserGroups(userGroups);
    }
}
