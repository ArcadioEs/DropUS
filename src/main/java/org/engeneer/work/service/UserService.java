package org.engeneer.work.service;

import org.engeneer.work.model.UserEntity;

import java.util.List;


/**
 * Service implementation to retireve {@link UserEntity}.
 */
public interface UserService {

	/**
	 *
	 */
	UserEntity getUserByUsername(final String username);

	/**
	 * Adds new user to the system.
	 */
	void saveUser(final String username, final String password, boolean isAdmin);

	/**
	 * Gets all users from the system.
	 */
	List<UserEntity> getAllUsers();

	/**
	 * Deletes user with given username.
	 * If user is found and deleted - returns true, false otherwise.
	 */
	boolean deleteUser(final String username);
}
