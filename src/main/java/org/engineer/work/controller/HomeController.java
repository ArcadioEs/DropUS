package org.engineer.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home page controller.
 */
@Controller
public class HomeController {

    @RequestMapping(value = {"/home", "/"})
    public String getHomePage(Model model) {
        model.addAttribute("welcomeMessage", "Welcome in DropUS!");

        return "home";
    }
}
