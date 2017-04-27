package org.engeneer.work.controller;

import org.engeneer.work.repository.UserRepository;
import org.engeneer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Initialization controller to insert admin user if it does not exist.
 *
 * TODO: Only for test pusposed, eventually will be removed.
 */
@Controller
public class InitController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@RequestMapping(value = "init")
	public String hello(@AuthenticationPrincipal User user) {

		/** Adding initial admin user, will be changed later on */
		if (userRepository.findByUsername("admin") == null) {
			userService.saveUser("admin", "nimda", true);
		}

		return "hello";
	}
}
