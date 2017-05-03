package org.engineer.work.service;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.UserEntity;

import java.util.List;


/**
 * Service used to add users to the system.
 */
public interface UserService {

	UserEntity getUserByUsername(final String username);

	/**
	 * Returns true if user is saved properly, false otherwise.
	 */
	boolean saveUser(final UserDTO userDTO, final boolean isAdmin);

	void updateUser(final UserEntity userEntity);

	List<UserEntity> getAllUsers();

	/**
	 * Returns true if user deleted properly, false otherwise.
	 */
	boolean deleteUser(final String username);
}
