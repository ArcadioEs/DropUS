package org.engineer.work.service;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.UserEntity;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.engineer.work.repository.UserRepository;
import org.engineer.work.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

/**
 * Test of {@link UserService} implementation.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    /**
     * Name constructed this way to avoid collision with real user name.
     * User can not create account with more than 15 digits within username.
     */
    private static final String MOCK_USERNAME = "User123456789012";
    private static final String MOCK_FILES_LOCATION = "dropus.files.root";

    @InjectMocks
    private final UserService userService = new UserServiceImpl();
    private UserDTO testUser;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PropertiesService propertiesService;
    @Mock
    private UserGroupsService userGroupsService;

    @Test
    public void shouldReturnUserByUsername() {
        testUser = getCompleteUserDTO(MOCK_USERNAME);

        Mockito.when(userRepository.findOne(MOCK_USERNAME)).thenReturn(new UserEntity(testUser));

        Assert.assertEquals(testUser.getUsername(), userService.getUserByUsername(MOCK_USERNAME).getUsername());
        Assert.assertEquals(testUser.getPassword(), userService.getUserByUsername(MOCK_USERNAME).getPassword());
        Assert.assertEquals(testUser.getRole(), userService.getUserByUsername(MOCK_USERNAME).getRole());
    }

    @Test
    public void shouldReturnAllUsersAsAList() {
        UserDTO userDTO = getCompleteUserDTO(MOCK_USERNAME + "0");
        final UserEntity user0 = new UserEntity(userDTO);

        userDTO = getCompleteUserDTO(MOCK_USERNAME + "1");
        final UserEntity user1 = new UserEntity(userDTO);

        final Iterable<UserEntity> users = new ArrayIterator<>(new UserEntity[]{user0, user1});

        Mockito.when(userRepository.findAll()).thenReturn(users);

        final List<UserEntity> userList = userService.getAllUsers();

        Assert.assertEquals(2, userList.size());
        Assert.assertTrue(userList.contains(user0));
        Assert.assertTrue(userList.contains(user1));
    }

    @Test
    public void shouldCreateUser() {
        testUser = getCompleteUserDTO(MOCK_USERNAME);

        Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(false);
        Mockito.when(propertiesService.getProperty(MOCK_FILES_LOCATION)).thenReturn("");

        Assert.assertTrue(userService.createUser(testUser));
    }

    @Test
    public void shouldReturnFalseDuringCreation() {
        testUser = getCompleteUserDTO(MOCK_USERNAME);

        Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(true);

        Assert.assertFalse(userService.createUser(testUser));
    }

    @Test
    public void shouldDeleteUser() {
        testUser = getCompleteUserDTO(MOCK_USERNAME);

        Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(true);
        Mockito.when(userGroupsService.deleteUserGroups(testUser.getUsername())).thenReturn(true);

        Assert.assertTrue(userService.deleteUser(testUser.getUsername()));
    }

    @Test
    public void shouldReturnFalseDuringDeletion() {
        testUser = getCompleteUserDTO(MOCK_USERNAME);

        Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(false);

        Assert.assertFalse(userService.deleteUser(testUser.getUsername()));
    }

    @Test
    public void shouldUpdateUser() {
        testUser = getCompleteUserDTO(MOCK_USERNAME);

        Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(true);

        Assert.assertTrue(userService.updateUser(new UserEntity(testUser)));
    }

    @Test
    public void shouldReturnFalseDuringUpdating() {
        testUser = getCompleteUserDTO(MOCK_USERNAME);

        Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(false);

        Assert.assertFalse(userService.updateUser(new UserEntity(testUser)));
    }

    private UserDTO getCompleteUserDTO(final String username) {
        final UserDTO userDTO = new UserDTO();

        userDTO.setUsername(username);
        userDTO.setPassword("test");
        userDTO.setEnabled((byte) 0);
        userDTO.setRole(AuthorityRoles.USER);

        return userDTO;
    }
}
