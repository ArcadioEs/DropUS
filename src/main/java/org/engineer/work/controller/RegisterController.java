package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.MessageFormat;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_LOGIN_PAGE;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_REGISTRATION_PAGE;

/**
 * Register controller.
 */
@Controller
@RequestMapping(value = "/registration")
public class RegisterController extends AbstractController {

    @RequestMapping(value = "/page")
    public String getRegistrationPage() {
        return TEMPLATE_REGISTRATION_PAGE;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@RequestParam("username") final String username,
                               @RequestParam("password") final String password,
                               @RequestParam("passwordConfirm") final String passwordConfirm,
                               final Model model) {
        String returnTemplate = TEMPLATE_REGISTRATION_PAGE;

        if (this.validateCredentials(model, username, password, passwordConfirm)) {
            final UserDTO userDTO = new UserDTO();

            userDTO.setUsername(username);
            userDTO.setPassword(getPasswordEncoder().encode(password));
            userDTO.setRole(AuthorityRoles.USER);
            userDTO.setEnabled((byte) 1);

            if (getUserFacade().createUser(userDTO)) {
                returnTemplate = TEMPLATE_LOGIN_PAGE;
            } else {
                model.addAttribute("userExists", MessageFormat.format("Username {0} already in use", username));
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

        if (username != null && getUserFacade().userExists(username)) {
            model.addAttribute("usernameError", "Username already in use!");
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
