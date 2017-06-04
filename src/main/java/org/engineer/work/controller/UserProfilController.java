package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.util.StringUtils;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_USER_PROFILE;

/**
 * Controller to manage user's profile.
 */
@Controller
@RequestMapping("/profile")
public class UserProfilController extends AbstractController {

    @RequestMapping(value = "/display/{username}", method = RequestMethod.GET)
    public String getProfile(@PathVariable(value = "username") final String username, final Model model) {
        final UserDTO user = getUserFacade().getUserByUsername(StringUtils.capitalize(username.toLowerCase()));
        if (user != null) {
            model.addAttribute("userExists", true);
            model.addAttribute("userDetails", user);
        } else {
            model.addAttribute("userExists", false);
        }
        return TEMPLATE_USER_PROFILE;
    }
}
