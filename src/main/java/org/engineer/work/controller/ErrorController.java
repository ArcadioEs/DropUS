package org.engineer.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for denied view.
 */
@Controller
public class ErrorController {

    @RequestMapping(value = "/denied")
    public String getErrorPage() {
        return "fragments/error";
    }
}
