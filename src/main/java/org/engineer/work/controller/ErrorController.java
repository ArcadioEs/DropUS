package org.engineer.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_ERROR_PAGE;

/**
 * Controller for error page view.
 */
@Controller
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private static final String ERROR_PATH = "/error";

    @GetMapping(value = ERROR_PATH)
    public String error() {
        return TEMPLATE_ERROR_PAGE;
    }

    @GetMapping(value = "/denied")
    public String getDeniedPage() {
        return TEMPLATE_ERROR_PAGE;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
