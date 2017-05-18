package org.engineer.work.controller;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.facade.GroupFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for wall page.
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupFacade groupFacade;

    @RequestMapping("/page")
    public String getGroupPage(final Model model) {
        model.addAttribute("created", false);
        model.addAttribute("allgroups", groupFacade.getAllGroups());

        return "groups";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createGroup(@RequestParam(value = "name") final String name,
                              @RequestParam(value = "description") final String description,
                              @AuthenticationPrincipal User user,
                              final Model model) {
        if (user != null && validate(model, name, description)) {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setName(name);
            groupDTO.setGroupOwner(user.getUsername());

            if (groupFacade.createGroup(groupDTO)) {
                model.addAttribute("created", true);
            } else {
                model.addAttribute("groupCreationFailed", "Creating group failed for some reason, please try later");
            }
        }
        return "groups";
    }

    private boolean validate(final Model model, final String name, final String description) {
        boolean result = true;

        if (name == null || name.isEmpty()) {
            model.addAttribute("nameError", "Group name cannot be empty!");
            result = false;
        }

        if (name != null && groupFacade.groupExists(name)) {
            model.addAttribute("nameError", "Group with this name already exists!");
            result = false;
        }

        if (description == null || description.isEmpty()) {
            model.addAttribute("descriptionError", "Description cannot be empty!");
            result = false;
        }

        return result;
    }
}
