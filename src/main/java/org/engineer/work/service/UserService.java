package org.engineer.work.service;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.exception.user.UserExistsException;
import org.engineer.work.exception.user.UserNotFoundException;
import org.engineer.work.model.UserEntity;

import java.util.List;


/**
 * Service managing users in the system.
 */
public interface UserService {

	UserEntity getUserByUsername(final String username);

	/**
	 * Creates user if not exist, throw exception otherwise.
	 */
	void createUser(final UserDTO userDTO) throws UserExistsException;

	void updateUser(final UserEntity userEntity) throws UserNotFoundException;

	List<UserEntity> getAllUsers();

	/**
	 * Deletes user if exist, throw exception otherwise.
	 */
	void deleteUser(final String username) throws UserNotFoundException;
}
