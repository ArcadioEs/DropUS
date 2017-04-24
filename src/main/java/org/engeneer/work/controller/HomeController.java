package org.engeneer.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by i332319 on 24/04/17.
 */
@Controller
public class HomeController {

	@RequestMapping(value = {"home", "/"})
	public String getHomePage(Model model) {
		model.addAttribute("welcomeMessage", "Welcome in DropUS!");

		return "home";
	}
}
