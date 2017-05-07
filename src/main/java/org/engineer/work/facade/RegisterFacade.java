package org.engineer.work.facade;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.exception.user.UserExistsException;

/**
 * Facade used to register new users.
 */
public interface RegisterFacade {

	void registerUser(final UserDTO userDTO) throws UserExistsException;
}
