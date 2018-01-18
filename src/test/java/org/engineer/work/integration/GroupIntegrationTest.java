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

    private final String OTHER_TEST_USER = "Other_test_user";

    private UserDTO userDTO;
    private GroupDTO groupDTO;

    @Before
    public void setUp() {
        userDTO = getCompleteUserDTO(MOCK_USERNAME);
        groupDTO = getCompleteGroupDTO(MOCK_GROUP_NAME);
    }

    @After
    public void cleanUp() {
        getUserFacade().deleteUser(MOCK_USERNAME);
        getUserFacade().deleteUser(OTHER_TEST_USER);
    }

    @Test
    public void checkGroupMechanism() {
        getUserFacade().createUser(userDTO);
        getGroupFacade().createGroup(groupDTO);

        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getName().equals(MOCK_GROUP_NAME));
        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getGroupOwner().equals(MOCK_USERNAME));
        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getDescription().equals(MOCK_GROUP_DESCRIPTION));

        assertTrue(getGroupFacade().getUserGroups(MOCK_USERNAME).get(0).getName().equals(groupDTO.getName()));
        assertTrue(getGroupFacade().getUserGroups(MOCK_USERNAME).get(0).getGroupOwner().equals(groupDTO.getGroupOwner()));
        assertTrue(getGroupFacade().getUserGroups(MOCK_USERNAME).get(0).getDescription().equals(groupDTO.getDescription()));

        final UserDTO otherUser = getCompleteUserDTO(OTHER_TEST_USER);
        getUserFacade().createUser(otherUser);

        getGroupFacade().updatePendingUsers(OTHER_TEST_USER, MOCK_GROUP_NAME, true);
        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getPendingUsers().contains(OTHER_TEST_USER));

        getGroupFacade().updateGroupMembers(OTHER_TEST_USER, MOCK_GROUP_NAME, true);
        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getMembers().contains(OTHER_TEST_USER));

        getGroupFacade().updateGroupMembers(OTHER_TEST_USER, MOCK_GROUP_NAME, false);
        assertFalse(getGroupFacade().getGroupByName(MOCK_GROUP_NAME).getMembers().contains(OTHER_TEST_USER));

        assertTrue(getGroupFacade().getAllGroups().stream().map(g -> g.getName().equals(groupDTO.getName())).findFirst() != null);

        getGroupFacade().deleteGroup(MOCK_GROUP_NAME);

        assertTrue(getGroupFacade().getGroupByName(MOCK_GROUP_NAME) == null);
    }
}
