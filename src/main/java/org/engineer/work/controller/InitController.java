package org.engineer.work.controller;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.repository.UserRepository;
import org.engineer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/init")
	public String hello() {

		/** Adding initial admin user, will be changed later on */
		if (userService.getUserByUsername("admin") == null) {
			final UserDTO userDTO = new UserDTO();
			userDTO.setUsername("admin");
			userDTO.setPassword(passwordEncoder.encode("nimda"));
			userDTO.setEnabled((byte) 1);
			userService.saveUser(userDTO, true);
		}

		return "home";
	}
}
