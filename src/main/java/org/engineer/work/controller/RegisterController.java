package org.engineer.work.controller;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.exception.user.UserExistsException;
import org.engineer.work.facade.RegisterFacade;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Register controller.
 */
@Controller
@RequestMapping(value = "/registration")
public class RegisterController {

	@Autowired
	private RegisterFacade registerFacade;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/page")
	public String getRegistrationPage() {
		return "registration";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(@RequestParam("username") final String username,
							   @RequestParam("password") final String password,
							   @RequestParam("passwordConfirm") final String passwordConfirm,
							   final Model model) {
		String returnTemplate = "registration";

		if (this.validateCredentials(model, username, password, passwordConfirm)) {
			final UserDTO userDTO = new UserDTO();

			userDTO.setUsername(username);
			userDTO.setPassword(passwordEncoder.encode(password));
			userDTO.setRole(AuthorityRoles.USER);
			userDTO.setEnabled((byte) 1);

			try {
				registerFacade.registerUser(userDTO);
				returnTemplate = "login";
			} catch (UserExistsException e) {
				model.addAttribute("userExists", e.getMessage());
			}
		}

		return returnTemplate;
	}

	private boolean validateCredentials(final Model model, final String username, final String password, final String passwordConfirm) {
		boolean result = true;

		if (username == null || username.isEmpty()) {
			model.addAttribute("usernameError", "Username cannot be empty!");
			result = false;
		}

		if (username != null && username.toLowerCase().contains("admin")) {
			model.addAttribute("usernameError", "Username cannot contain \"admin\" keyword!");
			result = false;
		}

		if (password == null || password.isEmpty()) {
			model.addAttribute("passwordError", "Password cannot be empty!");
			result = false;
		}

		if (passwordConfirm == null || !passwordConfirm.equals(password)) {
			model.addAttribute("confirmError", "Password should be identical!");
			result = false;
		}

		return result;
	}
}
