package org.engineer.work.service;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.UserEntity;
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

	@InjectMocks
	private UserService userService = new UserServiceImpl();
	private UserDTO testUser;

	@Mock
	private UserRepository userRepository;

	@Test
	public void shouldReturnAllUsersAsAList() {
		final UserDTO userDTO = new UserDTO();
		userDTO.setUsername("User0");
		final UserEntity user0 = new UserEntity(userDTO);

		userDTO.setUsername("User1");
		final UserEntity user1 = new UserEntity(userDTO);

		final Iterable<UserEntity> users = new ArrayIterator<>(new UserEntity[] {user0, user1});

		Mockito.when(userRepository.findAll()).thenReturn(users);

		final List<UserEntity> userList = userService.getAllUsers();

		Assert.assertEquals(userList.size(), 2);
		Assert.assertTrue(userList.contains(user0));
		Assert.assertTrue(userList.contains(user1));
	}

	@Test
	public void shouldCreateUser() {
		testUser = new UserDTO();
		testUser.setUsername("User");

		Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(false);

		Assert.assertTrue(userService.createUser(testUser));
	}

	@Test
	public void shouldReturnFalseDuringCreation() {
		testUser = new UserDTO();
		testUser.setUsername("User");

		Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(true);

		Assert.assertFalse(userService.createUser(testUser));
	}

	@Test
	public void shouldDeleteUser() {
		testUser = new UserDTO();
		testUser.setUsername("User");

		Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(true);

		Assert.assertTrue(userService.deleteUser(testUser.getUsername()));
	}

	@Test
	public void shouldReturnFalseDuringDeletion() {
		testUser = new UserDTO();
		testUser.setUsername("User");

		Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(false);

		Assert.assertFalse(userService.deleteUser(testUser.getUsername()));
	}

	@Test
	public void shouldUpdateUser() {
		testUser = new UserDTO();
		testUser.setUsername("User");

		Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(true);

		Assert.assertTrue(userService.updateUser(new UserEntity(testUser)));
	}

	@Test
	public void shouldReturnFalseDuringUpdating() {
		testUser = new UserDTO();
		testUser.setUsername("User");

		Mockito.when(userRepository.exists(testUser.getUsername())).thenReturn(false);

		Assert.assertFalse(userService.updateUser(new UserEntity(testUser)));
	}
}
