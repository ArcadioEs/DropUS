package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.REDIRECTION_PREFIX;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_ADMIN_PANEL;

/**
 * Admin perspective view.
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController extends AbstractController {

    @GetMapping(value = "/page")
    public String getAdminView(final Model model) {
        model.addAttribute("allusers", getUserFacade().getRegularUsers());

        return TEMPLATE_ADMIN_PANEL;
    }

    @PostMapping(value = "/update")
    public String updateUser(@RequestParam("enabled") final boolean enabled,
                             @RequestParam(required = false, value = "usernames[]") final String[] usernames) {
        if (usernames != null) {
            for (final String username : usernames) {
                getUserFacade().updateUserEnabledStatus(username, enabled);
            }
        }
        return REDIRECTION_PREFIX + "/admin/page";
    }
}
