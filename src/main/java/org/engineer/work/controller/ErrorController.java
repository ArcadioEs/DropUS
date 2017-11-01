package org.engineer.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for denied view.
 */
@Controller
public class ErrorController {

    @GetMapping(value = "/denied")
    public String getErrorPage() {
        return "fragments/error";
    }
}
