package org.engeneer.work.service;

import org.engeneer.work.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *	Service implementation to retireve {@link UserEntity}.
 */
@Service
public interface UserService {

	/**
	 * Gets specific user for given id.
	 */
	UserEntity getUserById(final Long userId);

	/**
	 * Gets specific user for given username.
	 */
	UserEntity getUserByUsername(final String username);

	/**
	 * Gets specific user for given username.
	 */
	void saveUser(final String username);

	/**
	 * Gets all users from database.
	 */
	List<UserEntity> getAllUsers();

	/**
	 * Deletes user with given username.
	 * If user will be found and deleted returns true, false otherwise.
	 */
	boolean deleteUser(final Long userId);
}
