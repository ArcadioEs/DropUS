package org.engineer.work.service;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.UserEntity;

import java.util.List;


/**
 * Service managing users in the system.
 */
public interface UserService {

	UserEntity getUserByUsername(final String username);

	boolean createUser(final UserDTO userDTO);

	boolean updateUser(final UserEntity userEntity);

	List<UserEntity> getAllUsers();

	boolean deleteUser(final String username);
}
