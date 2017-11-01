package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.UserDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.DISPLAY_ALL_GROUPS;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.DISPLAY_GROUP;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.REDIRECTION_PREFIX;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_GROUPS;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_SPECIFIC_GROUP;
import static org.thymeleaf.util.StringUtils.capitalize;

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

    @GetMapping(value = "/page")
    public String getGroupPage(@AuthenticationPrincipal final User user,
                               final Model model) {
        this.loadGroupLists(user, model);

        return TEMPLATE_GROUPS;
    }

    @GetMapping(value = "/display/{groupName}")
    public String getSpecificGroup(@PathVariable(value = "groupName") final String groupName,
                                   @AuthenticationPrincipal final User user,
                                   final Model model) {
        final String validGroupName = capitalize(groupName.trim().toLowerCase());
        final GroupDTO group = getGroupFacade().getGroupByName(validGroupName);

        if (user != null && group != null) {
            this.determineUserRoleInGroup(user.getUsername(), validGroupName, model);
            if (group.getMembers() != null) {
                group.getMembers().removeIf(member -> member.equals(group.getGroupOwner()));
            }
            model.addAttribute("group", group);
            model.addAttribute("posts", getPostFacade().getPostsForSpecificGroup(validGroupName));
            model.addAttribute("groupExists", true);
        } else {
            model.addAttribute("groupExists", false);
        }
        return TEMPLATE_SPECIFIC_GROUP;
    }

    @PostMapping(value = "/join")
    public String joinGroup(@RequestParam(value = "groupName") final String groupName,
                            @RequestParam(value = "add") final String decision,
                            @AuthenticationPrincipal final User user,
                            final Model model) {
        final String validGroupName = capitalize(groupName.trim().toLowerCase());
        if (user != null) {
            final boolean add = Boolean.parseBoolean(decision);
            getGroupFacade().updatePendingUsers(user.getUsername(), validGroupName, add);
        }
        return REDIRECTION_PREFIX + DISPLAY_GROUP + validGroupName;
    }

    @PostMapping(value = "/accept")
    public String acceptUserToGroup(@RequestParam(value = "username") final String username,
                                    @RequestParam(value = "groupName") final String groupName,
                                    @AuthenticationPrincipal final User user,
                                    final Model model) {
        final String validGroupName = capitalize(groupName.trim().toLowerCase());
        if (user != null && ADMIN.equals(this.determineUserRoleInGroup(user.getUsername(), validGroupName, model))) {
            getGroupFacade().updateGroupMembers(username, validGroupName, TRUE);
        }
        return REDIRECTION_PREFIX + DISPLAY_GROUP + validGroupName;
    }

    @PostMapping(value = "/exit")
    public String exitGroup(@RequestParam("groupName") final String groupName,
                             @AuthenticationPrincipal final User user,
                             final Model model) {
        final String validGroupName = capitalize(groupName.trim().toLowerCase());
        if (user != null && MEMBER.equals(this.determineUserRoleInGroup(user.getUsername(), validGroupName, model))) {
            getGroupFacade().updateGroupMembers(user.getUsername(), validGroupName, FALSE);
        }

        return REDIRECTION_PREFIX + DISPLAY_ALL_GROUPS;
    }

    @PostMapping(value = "/create")
    public String createGroup(@RequestParam(value = "groupName") final String groupName,
                              @RequestParam(value = "description") final String description,
                              @AuthenticationPrincipal final User user,
                              final Model model) {
        final String validGroupName = capitalize(groupName.trim().toLowerCase());
        if (user != null && this.validate(model, validGroupName, description)) {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setName(validGroupName);
            groupDTO.setGroupOwner(user.getUsername());
            groupDTO.setDescription(description);

            if (getGroupFacade().createGroup(groupDTO)) {
                return REDIRECTION_PREFIX + DISPLAY_GROUP + validGroupName;
            } else {
                model.addAttribute("groupCreationFailed", "Creating group failed for some reason, please try later");
            }
        }
        return REDIRECTION_PREFIX + DISPLAY_ALL_GROUPS;
    }

    @PostMapping(value = "/delete")
    public String deleteGroup(@RequestParam(value = "groupName") final String groupName,
                              @AuthenticationPrincipal final User user,
                              final Model model) {
        final String validGroupName = capitalize(groupName.trim().toLowerCase());
        final GroupDTO group = getGroupFacade().getGroupByName(validGroupName);
        if (user != null && group != null && ADMIN.equals(this.determineUserRoleInGroup(user.getUsername(), group.getName(), model))) {
            if (getGroupFacade().deleteGroup(validGroupName)) {
                model.addAttribute("groupDeletion", "Group deleted successfully");
            } else {
                model.addAttribute("groupDeletion", "Group couldn't be deleted");
            }
        }
        return REDIRECTION_PREFIX + DISPLAY_ALL_GROUPS;
    }

    private boolean validate(final Model model, final String name, final String description) {
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

        return result;
    }

    private void loadGroupLists(final User user, final Model model) {
        final List<GroupDTO> allGroups = getGroupFacade().getAllGroups();
        if (user != null) {
            final List<GroupDTO> userGroups = getGroupFacade().getUserGroups(user.getUsername());
            if (userGroups != null) {
                final List<String> userGroupsList = userGroups.stream().map(group -> group.getName()).collect(Collectors.toList());
                allGroups.removeIf(group -> userGroupsList.contains(group.getName()));
                model.addAttribute("userGroups", userGroups);
            }
        }
        model.addAttribute("allGroups", allGroups);
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
                    if (userDTO.getUserGroups().contains(groupDTO.getName())) {
                        model.addAttribute(MEMBER, true);
                        role = MEMBER;
                    } else {
                        if (groupDTO.getPendingUsers().contains(userDTO.getUsername())) {
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
