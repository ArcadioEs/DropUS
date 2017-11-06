package org.engineer.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home page controller.
 */
@Controller
@RequestMapping(value = {"/home", "/"})
public class HomeController {

    @GetMapping
    public String getHomePage(final Model model) {
        model.addAttribute("welcomeMessage", "Welcome in DropUS!");

        return "home";
    }
}
