package org.engineer.work.controller.abstractcontroller;

import org.engineer.work.facade.GroupFacade;
import org.engineer.work.facade.PostFacade;
import org.engineer.work.facade.StorageFacade;
import org.engineer.work.facade.UserFacade;
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
    private PostFacade postFacade;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private StorageFacade storageFacade;

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

    protected StorageFacade getStorageFacade() {
        return this.storageFacade;
    }

    public interface Templates {
        String REDIRECTION_PREFIX = "redirect:";

        //links
        String LOGIN_PAGE = "/login";

        String REGISTRATION_PAGE = "/registration";

        String DISPLAY_USER_PROFILE = "/profile/display/";

        String DISPLAY_GROUP = "/group/display/";
        String DISPLAY_ALL_GROUPS = "/group/page";

        // views
        String TEMPLATE_ERROR_PAGE = "fragments/error";

        String TEMPLATE_GROUPS = "groups";
        String TEMPLATE_SPECIFIC_GROUP = "specificgroup";

        String TEMPLATE_ADMIN_PANEL = "adminpanel";

        String TEMPLATE_REGISTRATION_PAGE = "registration";
        String TEMPLATE_LOGIN_PAGE = "login";

        String TEMPLATE_USER_PROFILE = "userprofile";
    }
}
