package org.engeneer.work.service;

import org.engeneer.work.model.UserEntity;

import java.util.List;


/**
 * Service used to add users to the system.
 */
public interface UserService {

	UserEntity getUserByUsername(final String username);

	/**
	 * Returns true if user is saved properly, false otherwise.
	 */
	boolean saveUser(final String username, final String password, boolean isAdmin);

	List<UserEntity> getAllUsers();

	/**
	 * Returns true if user deleted properly, false otherwise.
	 */
	boolean deleteUser(final String username);
}
