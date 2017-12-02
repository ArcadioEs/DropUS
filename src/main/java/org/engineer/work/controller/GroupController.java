package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.GroupDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.DISPLAY_ALL_GROUPS;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.DISPLAY_GROUP;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.REDIRECTION_PREFIX;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_ERROR_PAGE;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_GROUPS;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_SPECIFIC_GROUP;

/**
 * Controller for group page.
 */
@Controller
@RequestMapping("/group")
public class GroupController extends AbstractController {

    private static final String NAME_ERROR = "nameError";
    private static final String DESCRIPTION_ERROR = "descriptionError";

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
        final String validGroupName = validateName(groupName);
        final GroupDTO group = getGroupFacade().getGroupByName(validGroupName);

        if (user != null && group != null) {
            model.addAttribute(determineUserRoleInGroup(user.getUsername(), validGroupName), true);
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
                            @AuthenticationPrincipal final User user) {
        final String validGroupName = validateName(groupName);

        if (user != null) {
            final boolean add = Boolean.parseBoolean(decision);
            getGroupFacade().updatePendingUsers(user.getUsername(), validGroupName, add);
        }
        return REDIRECTION_PREFIX + DISPLAY_GROUP + validGroupName;
    }

    @PostMapping(value = "/accept")
    public String acceptUserToGroup(@RequestParam(value = "username") final String username,
                                    @RequestParam(value = "groupName") final String groupName,
                                    @AuthenticationPrincipal final User user) {
        final String validGroupName = validateName(groupName);
        final String userRole = determineUserRoleInGroup(user.getUsername(), validGroupName);

        if (ADMIN.equals(userRole)) {
            getGroupFacade().updateGroupMembers(username, validGroupName, TRUE);
        }
        return REDIRECTION_PREFIX + DISPLAY_GROUP + validGroupName;
    }

    @PostMapping(value = "/exit")
    public String exitGroup(@RequestParam("groupName") final String groupName,
                             @AuthenticationPrincipal final User user) {
        final String validGroupName = validateName(groupName);
        final String userRole = determineUserRoleInGroup(user.getUsername(), validGroupName);

        if (MEMBER.equals(userRole)) {
            getGroupFacade().updateGroupMembers(user.getUsername(), validGroupName, FALSE);
        }

        return REDIRECTION_PREFIX + DISPLAY_ALL_GROUPS;
    }

    @PostMapping(value = "/create")
    public String createGroup(@RequestParam(value = "groupName") final String groupName,
                              @RequestParam(value = "description") final String description,
                              @AuthenticationPrincipal final User user,
                              final RedirectAttributes redirectAttributes) {
        final Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        final Matcher m = p.matcher(groupName);

        if (m.find()) {
            redirectAttributes.addFlashAttribute(NAME_ERROR, "Group name cannot contain any special character");
        } else {
            final String validGroupName = validateName(groupName);
            if (user != null && this.validate(redirectAttributes, validGroupName, description)) {
                GroupDTO groupDTO = new GroupDTO();
                groupDTO.setName(validGroupName);
                groupDTO.setGroupOwner(user.getUsername());
                groupDTO.setDescription(description);

                if (getGroupFacade().createGroup(groupDTO)) {
                    return REDIRECTION_PREFIX + DISPLAY_GROUP + validGroupName;
                } else {
                    redirectAttributes.addFlashAttribute("groupCreationFailed", "Creating group failed for some reason, please try later");
                }
            }
        }

        return REDIRECTION_PREFIX + DISPLAY_ALL_GROUPS;
    }

    @PostMapping(value = "/delete")
    public String deleteGroup(@RequestParam(value = "groupName") final String groupName,
                              @AuthenticationPrincipal final User user,
                              final RedirectAttributes redirectAttributes) {
        final String validGroupName = validateName(groupName);
        final GroupDTO group = getGroupFacade().getGroupByName(validGroupName);
        final String userRole = determineUserRoleInGroup(user.getUsername(), validGroupName);

        if (group != null && ADMIN.equals(userRole)) {
            if (getGroupFacade().deleteGroup(validGroupName)) {
                redirectAttributes.addFlashAttribute("groupDeletion", "Group deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("groupDeletion", "Group couldn't be deleted");
            }
        }
        return REDIRECTION_PREFIX + DISPLAY_ALL_GROUPS;
    }

    @PostMapping(value = "/remove")
    public String removeUser(@RequestParam(value = "groupName") final String groupName,
                             @RequestParam(value = "userToRemove") final String userToRemove,
                             @AuthenticationPrincipal final User user) {
	    final String validUsername = validateName(userToRemove);
	    final String validGroupName = validateName(groupName);
	    final GroupDTO group = getGroupFacade().getGroupByName(validGroupName);
	    final String userRole = determineUserRoleInGroup(user.getUsername(), validGroupName);

	    if (group != null && ADMIN.equals(userRole)) {
	    	getGroupFacade().updateGroupMembers(validUsername, validGroupName, FALSE);
	    }

	    return REDIRECTION_PREFIX + DISPLAY_GROUP + validGroupName;
    }

    @ExceptionHandler(value = Exception.class)
    public String getErrorPage() {
        return TEMPLATE_ERROR_PAGE;
    }

    private boolean validate(final RedirectAttributes model, final String name, final String description) {
        boolean result = true;

        if (name == null || name.isEmpty()) {
            model.addFlashAttribute(NAME_ERROR, "Group name cannot be empty");
            result = false;
        }

        if (name != null && name.length() > 20) {
            model.addFlashAttribute(NAME_ERROR, "Group must not be bigger than 20 digits");
            result = false;
        }

        if (name != null && getGroupFacade().groupExists(name)) {
            model.addFlashAttribute(NAME_ERROR, "Group with this name already exists");
            result = false;
        }

        if (description == null || description.isEmpty()) {
            model.addFlashAttribute(DESCRIPTION_ERROR, "Description cannot be empty");
            result = false;
        }

        if (description != null && description.length() > 255) {
            model.addFlashAttribute(DESCRIPTION_ERROR, "Description must not be bigger than 255 digits");
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
}
