package org.engineer.work.facade;

import org.engineer.work.dto.UserDTO;

import java.util.List;

/**
 * Facade for user management.
 */
public interface UserFacade {

    boolean userIsAdmin(final String username);

    boolean userExists(final String username);

    UserDTO getUserByUsername(final String username);

    boolean createUser(final UserDTO userDTO);

    List<UserDTO> getAllUsers();

    List<UserDTO> getRegularUsers();

    boolean deleteUser(final String username);

    void updateUserEnabledStatus(final String username, final boolean enabled);
}
