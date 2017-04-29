package org.engineer.work.facade.impl;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.facade.UserFacade;
import org.engineer.work.model.UserEntity;
import org.engineer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of UserFacade.
 */
@Service
public class UserFacadeImpl implements UserFacade {

	@Autowired
	private UserService userService;

	@Override
	public UserDTO getUserByUsername(final String username) {
		return convertEntityToDTO(userService.getUserByUsername(username));
	}

	@Override
	public List<UserDTO> getAllUsers() {
		return userService.getAllUsers().stream().map(userEntity -> convertEntityToDTO(userEntity)).collect(Collectors.toList());
	}

	@Override
	public void updateUserEnabledStatus(final String username, final boolean enabled) {
		final UserEntity user = userService.getUserByUsername(username);

		if (user != null) {
			user.setEnabled(enabled ? (byte) 1 : (byte) 0);
			userService.saveUser(convertEntityToDTO(user), false);
		}
	}

	/**
	 * Converts given entity into DTO.
	 * @param userEntity entity received from service layer
	 * @return properly prepared DTO
	 */
	protected UserDTO convertEntityToDTO(final UserEntity userEntity) {
		final UserDTO userDTO = new UserDTO();

		userDTO.setUsername(userEntity.getUsername());
		userDTO.setEnabled(userEntity.getEnabled());

		return userDTO;
	}
}
