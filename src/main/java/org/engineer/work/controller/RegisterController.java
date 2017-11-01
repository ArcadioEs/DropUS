package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import java.text.MessageFormat;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.LOGIN_PAGE;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.REDIRECTION_PREFIX;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.REGISTRATION_PAGE;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_REGISTRATION_PAGE;

/**
 * Register controller.
 */
@Controller
@RequestMapping(value = "/registration")
public class RegisterController extends AbstractController {

    @GetMapping
    public String getRegistrationPage() {
        return TEMPLATE_REGISTRATION_PAGE;
    }

    @PostMapping(value = "/register")
    public String registerUser(@RequestParam("username") final String username,
                               @RequestParam("password") final String password,
                               @RequestParam("passwordConfirm") final String passwordConfirm,
                               final RedirectAttributes model) {
        final String validUsername = StringUtils.capitalize(username.trim().toLowerCase());

        if (this.validateCredentials(model, validUsername, password, passwordConfirm)) {
            final UserDTO userDTO = new UserDTO();

            userDTO.setUsername(validUsername);
            userDTO.setPassword(getPasswordEncoder().encode(password));
            userDTO.setRole(AuthorityRoles.USER);
            userDTO.setEnabled((byte) 1);

            if (getUserFacade().createUser(userDTO)) {
                model.addFlashAttribute("userRegistered", userDTO.getUsername());
                return REDIRECTION_PREFIX + LOGIN_PAGE;
            } else {
                model.addFlashAttribute("userExists", MessageFormat.format("Username {0} already in use", validUsername));
            }
        }

        return REDIRECTION_PREFIX + REGISTRATION_PAGE;
    }

    private boolean validateCredentials(final RedirectAttributes model, final String username, final String password, final String passwordConfirm) {
        boolean result = true;

        if (username == null || username.isEmpty()) {
            model.addFlashAttribute("usernameError", "Username cannot be empty!");
            result = false;
        }

        if (username != null && getUserFacade().userExists(username)) {
            model.addFlashAttribute("usernameError", "Username already in use!");
            result = false;
        }

        if (username != null && username.toLowerCase().contains("admin")) {
            model.addFlashAttribute("usernameError", "Username cannot contain \"admin\" keyword!");
            result = false;
        }

        if (password == null || password.isEmpty()) {
            model.addFlashAttribute("passwordError", "Password cannot be empty!");
            result = false;
        }

        if (passwordConfirm == null || !passwordConfirm.equals(password)) {
            model.addFlashAttribute("confirmError", "Password should be identical!");
            result = false;
        }

        return result;
    }
}
