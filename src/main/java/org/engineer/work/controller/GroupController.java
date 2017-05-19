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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createGroup(@RequestParam(value = "name") final String name,
                              @RequestParam(value = "description") final String description,
                              @AuthenticationPrincipal User user,
                              final Model model) {
        String returnTemplate = TEMPLATE_GROUPS;

        if (user != null && this.validate(user, model, name, description)) {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setName(name);
            groupDTO.setGroupOwner(user.getUsername());

            if (getGroupFacade().createGroup(groupDTO)) {
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
                model.addAttribute("groupdeletion", "Group deleted successfully");
            } else {
                model.addAttribute("groupdeletion", "Group couldn't be deleted");
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

        if (!result) {
            this.loadGroupLists(user, model);
        }

        return result;
    }

    private void loadGroupLists(final User user, final Model model) {
        model.addAttribute("allgroups", getGroupFacade().getAllGroups());
        if (user != null) {
            model.addAttribute("usergroups", getGroupFacade().getUserGroups(user.getUsername()));
        }
    }
}
