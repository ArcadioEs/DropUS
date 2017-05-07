package org.engineer.work.facade.impl;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.exception.user.UserExistsException;
import org.engineer.work.facade.RegisterFacade;
import org.engineer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RegisterFacadeImpl implements RegisterFacade {

	@Autowired
	private UserService userService;

	@Override
	public void registerUser(final UserDTO userDTO) throws UserExistsException {
		userService.createUser(userDTO);
	}
}
