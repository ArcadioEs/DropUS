package org.engeneer.work.services;

import org.engeneer.work.entities.UserEntity;
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
	UserEntity getUserById(final Long id);

	/**
	 * Gets specific user for given username.
	 */
	UserEntity saveUser(final String username);

	/**
	 * Gets all users from database.
	 */
	List<UserEntity> getAllUsers();
}
