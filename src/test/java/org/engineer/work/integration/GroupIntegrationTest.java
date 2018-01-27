package org.engineer.work.integration;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.integration.abstractlayer.AbstractIntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for group management feature.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupIntegrationTest extends AbstractIntegrationTest {

    private UserDTO userDTO;
	private UserDTO otherUserDTO;
    private GroupDTO groupDTO;

    @Before
    public void setUp() {
        userDTO = getCompleteUserDTO(MOCK_USERNAME);
	    otherUserDTO = getCompleteUserDTO(OTHER_TEST_USER);
        groupDTO = getCompleteGroupDTO(MOCK_GROUP_NAME);
    }

    @After
    public void cleanUp() {
	    getUserFacade().deleteUser(userDTO.getUsername());
	    getUserFacade().deleteUser(otherUserDTO.getUsername());
    }

    @Test
    public void checkGroupMechanism() {
        getUserFacade().createUser(userDTO);
        getGroupFacade().createGroup(groupDTO);

        /**
         * Checking basic information of created group
         */
        assertTrue(getGroupFacade().getGroupByName(groupDTO.getName()).getName().equals(groupDTO.getName()));
        assertTrue(getGroupFacade().getGroupByName(groupDTO.getName()).getGroupOwner().equals(userDTO.getUsername()));
        assertTrue(getGroupFacade().getGroupByName(groupDTO.getName()).getDescription().equals(MOCK_GROUP_DESCRIPTION));

        /**
         * Checking basic information of created usergroups
         */
        assertTrue(getGroupFacade().getUserGroups(userDTO.getUsername()).get(0).getName().equals(groupDTO.getName()));
        assertTrue(getGroupFacade().getUserGroups(userDTO.getUsername()).get(0).getGroupOwner().equals(groupDTO.getGroupOwner()));
        assertTrue(getGroupFacade().getUserGroups(userDTO.getUsername()).get(0).getDescription().equals(groupDTO.getDescription()));
        
        getUserFacade().createUser(otherUserDTO);

        /**
         * Adding user to pending group
         */
        getGroupFacade().updatePendingUsers(otherUserDTO.getUsername(), groupDTO.getName(), true);
        assertTrue(getGroupFacade().getGroupByName(groupDTO.getName()).getPendingUsers().contains(otherUserDTO.getUsername()));

        /**
         * Accepting user to group
         */
        getGroupFacade().updateGroupMembers(otherUserDTO.getUsername(), groupDTO.getName(), true);
        assertTrue(getGroupFacade().getGroupByName(groupDTO.getName()).getMembers().contains(otherUserDTO.getUsername()));

        /**
         * Removing user from group
         */
        getGroupFacade().updateGroupMembers(otherUserDTO.getUsername(), groupDTO.getName(), false);
        assertFalse(getGroupFacade().getGroupByName(groupDTO.getName()).getMembers().contains(otherUserDTO.getUsername()));

        assertTrue(getGroupFacade().getAllGroups().stream().map(g -> g.getName().equals(groupDTO.getName())).findFirst() != null);

        /**
         * Deleting group
         */
        getGroupFacade().deleteGroup(groupDTO.getName());

        assertTrue(getGroupFacade().getGroupByName(groupDTO.getName()) == null);
    }
}
