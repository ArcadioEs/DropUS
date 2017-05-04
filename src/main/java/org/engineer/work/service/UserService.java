package org.engineer.work.service;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.UserEntity;

import java.util.List;


/**
 * Service managing users in the system.
 */
public interface UserService {

	UserEntity getUserByUsername(final String username);

	/**
	 * Returns true if user is saved properly, false otherwise.
	 */
	boolean createUser(final UserDTO userDTO, final boolean isAdmin);

	void updateUser(final UserEntity userEntity);

	List<UserEntity> getAllUsers();

	/**
	 * Returns true if user deleted properly, false otherwise.
	 */
	boolean deleteUser(final String username);
}
