package org.engineer.work.controller.abstractcontroller;

import org.engineer.work.facade.GroupFacade;
import org.engineer.work.facade.RegisterFacade;
import org.engineer.work.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Abstract, generic controller to hold all repetitive data.
 */
@Controller
public abstract class AbstractController {

    @Resource
    private UserFacade userFacade;
    @Resource
    private GroupFacade groupFacade;
    @Resource
    private RegisterFacade registerFacade;
    @Resource
    private PasswordEncoder passwordEncoder;

    protected UserFacade getUserFacade() {
        return userFacade;
    }

    protected GroupFacade getGroupFacade() {
        return groupFacade;
    }

    protected RegisterFacade getRegisterFacade() {
        return registerFacade;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public interface Templates {
        String TEMPLATE_GROUPS = "groups";
        String TEMPLATE_SPECIFIC_GROUP = "specificgroup";

        String TEMPLATE_ADMIN_PANEL = "adminpanel";

        String TEMPLATE_REGISTRATION_PAGE = "registration";
        String TEMPLATE_LOGIN_PAGE = "login";
    }
}
