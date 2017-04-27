package org.engeneer.work.controller;

import org.engeneer.work.model.UserEntity;
import org.engeneer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller to display specific {@link UserEntity}.
 *
 * TODO: Refactor this for purposes related with user management (admin perspective).
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "add/{username}&{password}&{isAdmin}", method = RequestMethod.GET)
	public Map<String, Object> addUser(@PathVariable("username") String username, @PathVariable("password") String password, @PathVariable("isAdmin") boolean isAdmin) {

		final Map<String, Object> model = new HashMap<>();
		String message = "Both fields username and password must not be empty!";

		if (!username.isEmpty() && !password.isEmpty()) {
			userService.saveUser(username, password, isAdmin);
			message = "User " + username + " added to the system.";
		}

		model.put("message", message);
		return model;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String getAllUsers(@AuthenticationPrincipal User user) {
		if (user.getAuthorities().toString().contains("ADMIN")) {
			return "hello";
		} else {
			return "home";
		}
	}

	@RequestMapping(value = "/delete/{username}", method = RequestMethod.GET)
	public Map<String, Object> deleteUser(@PathVariable("username") String username) {

		final UserEntity user = userService.getUserByUsername(username);
		final Map<String, Object> model = new HashMap<>();
		String message = "User not found or could not be deleted.";

		if (user != null) {
			final String name = user.getUsername();
			if (userService.deleteUser(name)) {
				message = "User " + name + " removed from the system.";
			}
		}

		model.put("message", message);
		return model;
	}
}
