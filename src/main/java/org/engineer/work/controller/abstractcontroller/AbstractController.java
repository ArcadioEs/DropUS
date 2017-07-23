package org.engineer.work.controller.abstractcontroller;

import org.engineer.work.facade.GroupFacade;
import org.engineer.work.facade.PostFacade;
import org.engineer.work.facade.UserFacade;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Abstract, generic controller to hold all repetitive data.
 */
@Controller
public abstract class AbstractController {

    public static final String REDIRECT_PREFIX = "redirect:";

    @Resource
    private UserFacade userFacade;
    @Resource
    private GroupFacade groupFacade;
    @Resource
    private PostFacade postFacade;
    @Resource
    private PasswordEncoder passwordEncoder;

    protected UserFacade getUserFacade() {
        return this.userFacade;
    }

    protected GroupFacade getGroupFacade() {
        return this.groupFacade;
    }

    protected PostFacade getPostFacade() {
        return this.postFacade;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    public interface Templates {
        String TEMPLATE_GROUPS = "groups";
        String TEMPLATE_SPECIFIC_GROUP = "specificgroup";

        String TEMPLATE_ADMIN_PANEL = "adminpanel";

        String TEMPLATE_REGISTRATION_PAGE = "registration";
        String TEMPLATE_LOGIN_PAGE = "login";

        String TEMPLATE_USER_PROFILE = "userprofile";
    }
}
