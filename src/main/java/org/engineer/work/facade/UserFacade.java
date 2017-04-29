package org.engineer.work.facade;

import org.engineer.work.dto.UserDTO;

import java.util.List;

/**
 * Facade for user management.
 */
public interface UserFacade {
	UserDTO getUserByUsername(final String username);

	List<UserDTO> getAllUsers();

	void updateUserEnabledStatus(final String username, final boolean enabled);
}
