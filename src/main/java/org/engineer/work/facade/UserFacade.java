package org.engineer.work.facade;

import org.engineer.work.dto.UserDTO;

import java.util.List;

/**
 * Facade for user management.
 */
public interface UserFacade {

	/**
	 * Returns true if user is an admin, false otherwise.
	 */
	boolean userIsAdmin(final String username);

	UserDTO getUserByUsername(final String username);

	/**
	 * Returns list of all users, including admins.
	 */
	List<UserDTO> getAllUsers();

	/**
	 * Returns list of all regular users, excluding admins.
	 */
	List<UserDTO> getRegularUsers();

	void updateUserEnabledStatus(final String username, final boolean enabled);
}
