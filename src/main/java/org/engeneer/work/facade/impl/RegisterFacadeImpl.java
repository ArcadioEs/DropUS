package org.engeneer.work.facade.impl;

import org.engeneer.work.facade.RegisterFacade;
import org.engeneer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RegisterFacadeImpl implements RegisterFacade {

	@Autowired
	UserService userService;

	@Override
	public boolean registerUser(final String username, final String password) {
		return userService.saveUser(username, password, false);
	}
}
