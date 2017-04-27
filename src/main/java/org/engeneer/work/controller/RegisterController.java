package org.engeneer.work.controller;

import org.engeneer.work.facade.RegisterFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Register controller.
 */
@Controller
@RequestMapping(value = "/registration")
public class RegisterController {

	@Autowired
	RegisterFacade registerFacade;

	@RequestMapping(value = "/page")
	public String getRegistrationPage() {
		return "registration";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(final HttpServletRequest request, final Model model) {
		String returnTemplate = "registration";

		final String username = request.getParameter("username");
		final String password = request.getParameter("password");
		final String passwordConfirm = request.getParameter("passwordConfirm");

		if (validateCredentials(model, username, password, passwordConfirm)) {
			if (registerFacade.registerUser(username, password)) {
				returnTemplate = "login";
			} else {
				model.addAttribute("userExists", "User with this username already exists.");
			}
		}

		return returnTemplate;
	}

	private boolean validateCredentials(Model model, final String username, final String password, final String passwordConfirm) {
		boolean result = true;

		if (username == null || username.isEmpty()) {
			model.addAttribute("usernameError", "Username cannot be empty!");
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
