package org.engeneer.work.controllers;

import org.engeneer.work.entities.UserEntity;
import org.engeneer.work.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Controller to display specific {@link UserEntity}.
 */
@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "get/{id}", method = RequestMethod.GET)
	public String getUser(@PathVariable("id") Long id) {
		String error;
		try
		{
			return userService.getUserById(id).toString();
		}
		catch (Exception e) {
			error = "Something went wrong: " + e.getMessage();
			LOG.error(error);
		}
		return error;
	}

	@RequestMapping(value = "add/{username}", method = RequestMethod.GET)
	public String addUser(@PathVariable("username") String username) {
		userService.saveUser(username);

		return "DONE for " + username;
	}

	@RequestMapping(value = "/all")
	public List<UserEntity> getAllUsers() {
		return userService.getAllUsers();
	}
}
