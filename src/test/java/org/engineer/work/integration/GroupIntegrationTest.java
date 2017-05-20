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

import java.util.Arrays;

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

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

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
    public void shouldAssignUserToPendingTable() {
        userService.createUser(userDTO);
        groupService.createGroup(groupDTO);

        final UserEntity userEntity = userService.getUserByUsername(USER_NAME);
        userEntity.setGroupsPending(Arrays.asList(groupService.getGroupByName(GROUP_NAME)));
        userService.updateUser(userEntity);

        Assert.assertEquals(userService.getUserByUsername(USER_NAME).getGroupsPending().get(0).getName(), GROUP_NAME);
        Assert.assertEquals(groupService.getGroupByName(GROUP_NAME).getUsersPending().get(0).getUsername(), USER_NAME);

        groupService.deleteGroup(GROUP_NAME);
        userService.deleteUser(USER_NAME);

        Assert.assertTrue(groupService.getGroupByName(GROUP_NAME) == null);
        Assert.assertTrue(userService.getUserByUsername(USER_NAME) == null);
    }

    @Test
    public void shouldAssignGroupToUser() {
        userService.createUser(userDTO);
        groupService.createGroup(groupDTO);

        final GroupEntity userGroupToCheck = userService.getUserByUsername(USER_NAME).getGroups().get(0);

        Assert.assertEquals(userGroupToCheck.getName(), groupService.getGroupByName(GROUP_NAME).getName());
        Assert.assertEquals(userGroupToCheck.getGroupOwner(), groupService.getGroupByName(GROUP_NAME).getGroupOwner());

        groupService.deleteGroup(GROUP_NAME);
        userService.deleteUser(USER_NAME);

        Assert.assertTrue(userService.getUserByUsername(USER_NAME) == null);
        Assert.assertTrue(groupService.getGroupByName(GROUP_NAME) == null);
    }
}
