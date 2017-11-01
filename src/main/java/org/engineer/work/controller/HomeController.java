package org.engineer.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home page controller.
 */
@Controller
public class HomeController {

    @GetMapping(value = {"/home", "/"})
    public String getHomePage(Model model) {
        model.addAttribute("welcomeMessage", "Welcome in DropUS!");

        return "home";
    }
}
