package org.engineer.work.controller.abstractcontroller;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.UserDTO;
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

    protected static final String ADMIN = "isAdmin";
    protected static final String MEMBER = "isMember";
    protected static final String PENDING = "isPending";
    protected static final String NOT_PENDING = "isNotPending";

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

    /**
     * Check if given userRole matches either admin or member
     */
    protected boolean checkWhetherIsMemberGroup(final String userRole) {
        return ADMIN.equals(userRole) || MEMBER.equals(userRole);
    }

    /**
     * Returns user role in given group.
     * @return
     * <ul>
     * <li>isAdmin - if given user is admin of group</li>
     * <li>isMember - if given user is member of group</li>
     * <li>isPending - if user already wants to join group</li>
     * <li>isNotPending - if user is not connected with group in any way</li>
     * </ul>
     */
    protected String determineUserRoleInGroup(final String username, final String groupName) {
        if (username != null && groupName != null) {
            final UserDTO userDTO = getUserFacade().getUserByUsername(username);
            final GroupDTO groupDTO = getGroupFacade().getGroupByName(groupName);

            if (userDTO != null && groupDTO != null) {
                if (checkWhetherUserIsAdmin(userDTO, groupDTO) != null) {
                    return checkWhetherUserIsAdmin(userDTO, groupDTO);
                } else if (checkWhetherUserIsMember(userDTO, groupDTO) != null) {
                    return checkWhetherUserIsMember(userDTO, groupDTO);
                } else if (checkWhetherUserIsPending(userDTO, groupDTO) != null) {
                    return checkWhetherUserIsPending(userDTO, groupDTO);
                } else {
                    return NOT_PENDING;
                }
            }
        }
        return null;
    }

    private String checkWhetherUserIsAdmin(final UserDTO userDTO, final GroupDTO groupDTO) {
        String role = null;
        if (userDTO.getUsername().equals(groupDTO.getGroupOwner())) {
            role = ADMIN;
        }
        return role;
    }

    private String checkWhetherUserIsMember(final UserDTO userDTO, final GroupDTO groupDTO) {
        String role = null;
        if (userFacade.getUserByUsername(userDTO.getUsername()).getUserGroups().contains(groupDTO.getName())) {
            role = MEMBER;
        }
        return role;
    }

    private String checkWhetherUserIsPending(final UserDTO userDTO, final GroupDTO groupDTO) {
        String role = null;
        if (groupDTO.getPendingUsers().contains(userDTO.getUsername())) {
            role = PENDING;
        }
        return role;
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
