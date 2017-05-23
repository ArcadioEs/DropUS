package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.UserDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_GROUPS;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_SPECIFIC_GROUP;

/**
 * Controller for group page.
 */
@Controller
@RequestMapping("/group")
public class GroupController extends AbstractController {
    private static final String ADMIN = "isAdmin";
    private static final String MEMBER = "isMember";
    private static final String PENDING = "isPending";
    private static final String NOT_PENDING = "isNotPending";

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String getGroupPage(@AuthenticationPrincipal final User user,
                               final Model model) {
        this.loadGroupLists(user, model);

        return TEMPLATE_GROUPS;
    }

    @RequestMapping(value = "/display", method = RequestMethod.POST)
    public String getSpecificGroup(@RequestParam(value = "groupName") final String groupName,
                                   @AuthenticationPrincipal final User user,
                                   final Model model) {
        if (user != null) {
            this.determineUserRoleInGroup(user.getUsername(), groupName, model);
        }
        this.loadDataForSpecificGroup(groupName, model);
        return TEMPLATE_SPECIFIC_GROUP;
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public String joinGroup(@RequestParam(value = "groupName") final String groupName,
                            @RequestParam(value = "add") final String decision,
                            @AuthenticationPrincipal final User user,
                            final Model model) {
        if (user != null && groupName != null) {
            final boolean add = Boolean.parseBoolean(decision);
            getGroupFacade().updatePendingUsers(user.getUsername(), groupName, add);
            this.determineUserRoleInGroup(user.getUsername(), groupName, model);
        }
        this.loadDataForSpecificGroup(groupName, model);
        return TEMPLATE_SPECIFIC_GROUP;
    }

    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    public String acceptUserToGroup(@RequestParam(value = "username") final String username,
                                    @RequestParam(value = "groupName") final String groupName,
                                    @AuthenticationPrincipal final User user,
                                    final Model model) throws InterruptedException {
        if (user != null && ADMIN.equals(this.determineUserRoleInGroup(user.getUsername(), groupName, model))) {
            getGroupFacade().updateGroupMember(username, groupName);
        }
        this.loadDataForSpecificGroup(groupName, model);

        return TEMPLATE_SPECIFIC_GROUP;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createGroup(@RequestParam(value = "groupName") final String groupName,
                              @RequestParam(value = "description") final String description,
                              @AuthenticationPrincipal final User user,
                              final Model model) {
        String returnTemplate = TEMPLATE_GROUPS;

        if (user != null && this.validate(user, model, groupName, description)) {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setName(groupName);
            groupDTO.setGroupOwner(user.getUsername());
            groupDTO.setDescription(description);

            if (getGroupFacade().createGroup(groupDTO)) {
                this.loadDataForSpecificGroup(groupName, model);
                this.determineUserRoleInGroup(user.getUsername(), groupDTO.getName(), model);
                returnTemplate = TEMPLATE_SPECIFIC_GROUP;
            } else {
                model.addAttribute("groupCreationFailed", "Creating group failed for some reason, please try later");
            }
        }
        return returnTemplate;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteGroup(@RequestParam(value = "groupName") final String groupName,
                              @AuthenticationPrincipal final User user,
                              final Model model) {
        final GroupDTO group = getGroupFacade().getGroupByName(groupName);
        if (group != null && group.getGroupOwner().equals(user.getUsername())) {
            if (getGroupFacade().deleteGroup(groupName)) {
                model.addAttribute("groupDeletion", "Group deleted successfully");
            } else {
                model.addAttribute("groupDeletion", "Group couldn't be deleted");
            }
        }
        this.loadGroupLists(user, model);

        return TEMPLATE_GROUPS;
    }

    private boolean validate(final User user, final Model model, final String name, final String description) {
        boolean result = true;

        if (name == null || name.isEmpty()) {
            model.addAttribute("nameError", "Group name cannot be empty!");
            result = false;
        }

        if (name != null && name.length() > 20) {
            model.addAttribute("nameError", "Group must not be bigger than 20 digits");
            result = false;
        }

        if (name != null && getGroupFacade().groupExists(name)) {
            model.addAttribute("nameError", "Group with this name already exists!");
            result = false;
        }

        if (description == null || description.isEmpty()) {
            model.addAttribute("descriptionError", "Description cannot be empty!");
            result = false;
        }

        if (description != null && description.length() > 255) {
            model.addAttribute("descriptionError", "Description must not be bigger than 255 digits");
            result = false;
        }

        if (!result) {
            this.loadGroupLists(user, model);
        }

        return result;
    }

    private void loadGroupLists(final User user, final Model model) {
        final List<GroupDTO> allGroups = getGroupFacade().getAllGroups();
        if (user != null) {
            final List<GroupDTO> userGroups = getGroupFacade().getUserGroups(user.getUsername());
            if (userGroups != null) {
                allGroups.removeIf(group -> group.getGroupOwner().equals(user.getUsername()));
            }
            model.addAttribute("userGroups", userGroups);
        }
        model.addAttribute("allGroups", allGroups);
    }

    private void loadDataForSpecificGroup(final String groupName, final Model model) {
        if (groupName != null) {
            final GroupDTO group = getGroupFacade().getGroupByName(groupName);
            if (group != null) {
                model.addAttribute("groupAdmin", group.getGroupOwner());
                if (group.getUsers() != null) {
                    group.getUsers().removeIf(user -> user.equals(group.getGroupOwner()));
                }
                model.addAttribute("groupUsers", group.getUsers());
                model.addAttribute("groupName", group.getName());
                model.addAttribute("groupDescription", group.getDescription());
                model.addAttribute("usersPending", group.getPendings());
            }
        }
    }

    private String determineUserRoleInGroup(final String username, final String groupName, final Model model) {
        String role = null;
        if (username != null && groupName != null) {
            final UserDTO userDTO = getUserFacade().getUserByUsername(username);
            final GroupDTO groupDTO = getGroupFacade().getGroupByName(groupName);

            if (userDTO != null && groupDTO != null) {
                if (userDTO.getUsername().equals(groupDTO.getGroupOwner())) {
                    model.addAttribute(ADMIN, true);
                    role = ADMIN;
                } else {
                    if (userDTO.getGroups().contains(groupDTO.getName())) {
                        model.addAttribute(MEMBER, true);
                        role = MEMBER;
                    } else {
                        if (userDTO.getPendings().contains(groupDTO.getName())) {
                            model.addAttribute(PENDING, true);
                            role = PENDING;
                        } else {
                            model.addAttribute(NOT_PENDING, true);
                            role = NOT_PENDING;
                        }
                    }
                }
            }
        }
        return role;
    }
}
