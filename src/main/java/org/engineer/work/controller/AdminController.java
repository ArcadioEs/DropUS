package org.engineer.work.controller;

import org.engineer.work.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
		model.addAttribute("allusers", userFacade.getAllUsers());

		return "adminpanel";
	}
}
