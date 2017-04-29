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

	protected UserDTO convertEntityToDTO(final UserEntity userEntity) {
		final UserDTO userDTO = new UserDTO();

		userDTO.setUsername(userEntity.getUsername());
		userDTO.setEnabled(userEntity.getEnabled());

		return userDTO;
	}
}
