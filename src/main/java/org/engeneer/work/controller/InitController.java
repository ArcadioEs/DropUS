package org.engeneer.work.controller;

import org.engeneer.work.repository.UserRepository;
import org.engeneer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by i332319 on 22/04/17.
 */
@RestController
public class InitController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@RequestMapping(value = "init")
	public String init() {
		if (userRepository.findByUsername("admin") == null) {
			userService.saveUser("admin", "nimda", true);
		}
		return "System inited!";
	}
}
