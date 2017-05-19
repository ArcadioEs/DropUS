package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.GroupDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_GROUPS;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_SPECIFIC_GROUP;

/**
 * Controller for group page.
 */
@Controller
@RequestMapping("/group")
public class GroupController extends AbstractController {

    @RequestMapping("/page")
    public String getGroupPage(@AuthenticationPrincipal User user,
                               final Model model) {
        this.loadGroupLists(user, model);

        return TEMPLATE_GROUPS;
    }

    @RequestMapping("/display")
    public String getSpecificGroup(@RequestParam(value = "groupName") final String groupName,
                                   final Model model) {
        this.loadDataForSpecificGroup(groupName, model);

        return TEMPLATE_SPECIFIC_GROUP;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createGroup(@RequestParam(value = "groupName") final String groupName,
                              @RequestParam(value = "description") final String description,
                              @AuthenticationPrincipal User user,
                              final Model model) {
        String returnTemplate = TEMPLATE_GROUPS;

        if (user != null && this.validate(user, model, groupName, description)) {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setName(groupName);
            groupDTO.setGroupOwner(user.getUsername());
            groupDTO.setDescription(description);

            if (getGroupFacade().createGroup(groupDTO)) {
                this.loadDataForSpecificGroup(groupName, model);
                returnTemplate = TEMPLATE_SPECIFIC_GROUP;
            } else {
                model.addAttribute("groupCreationFailed", "Creating group failed for some reason, please try later");
            }
        }
        return returnTemplate;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteGroup(@RequestParam(value = "groupName") final String groupName,
                              @AuthenticationPrincipal User user,
                              final Model model) {
        if (getGroupFacade().getGroupByName(groupName) != null && getGroupFacade().getGroupByName(groupName).getGroupOwner().equals(user.getUsername())) {
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
        model.addAttribute("allGroups", getGroupFacade().getAllGroups());
        if (user != null) {
            model.addAttribute("userGroups", getGroupFacade().getUserGroups(user.getUsername()));
        }
    }

    private void loadDataForSpecificGroup(final String groupName, final Model model) {
        if (groupName != null) {
            final GroupDTO group = getGroupFacade().getGroupByName(groupName);
            if (group != null) {
                model.addAttribute("groupAdmin", group.getGroupOwner());
                model.addAttribute("groupUsers", group.getUsers());
                model.addAttribute("groupName", group.getName());
                model.addAttribute("groupDescription", group.getDescription());
            }
        }
    }
}
