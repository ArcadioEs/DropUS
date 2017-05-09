package org.engineer.work.controller;

import org.engineer.work.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Admin perspective view.
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	private UserFacade userFacade;

	@RequestMapping(value = "/page")
	public String getAdminView(final Model model) {
		model.addAttribute("allusers", userFacade.getRegularUsers());

		return "adminpanel";
	}

	@RequestMapping(value = "/update")
	public String updateUser(@RequestParam("enabled") final boolean enabled,
							 @RequestParam(required = false, value = "usernames[]") final String[] usernames,
							 final Model model) {
		if (usernames != null) {
			for (final String username : usernames) {
				userFacade.updateUserEnabledStatus(username, enabled);
			}
		}
		model.addAttribute("allusers", userFacade.getRegularUsers());

		return "adminpanel";
	}
}
