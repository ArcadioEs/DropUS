package org.engeneer.work.service;

import org.engeneer.work.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service implementation to retireve {@link UserEntity}.
 */
@Service
public interface UserService {

	/**
	 * Gets specific user for given id.
	 * @return
	 * 			{@link UserEntity} for given id if found, null otherwise.
	 */
	UserEntity getUserById(final Long userId);

	/**
	 * Adds new user to the system.
	 */
	void saveUser(final String username);

	/**
	 * Gets all users from the system.
	 */
	List<UserEntity> getAllUsers();

	/**
	 * Deletes user with given id.
	 * If user is found and deleted - returns true, false otherwise.
	 */
	boolean deleteUser(final Long userId);
}
