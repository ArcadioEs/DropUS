package org.engineer.work.facade;

import org.engineer.work.dto.UserDTO;

/**
 * Facade used to register new users.
 */
public interface RegisterFacade {

	boolean registerUser(final UserDTO userDTO);
}
