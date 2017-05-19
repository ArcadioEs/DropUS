package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_LOGIN_PAGE;

/**
 * Initialization controller to insert admin user if it does not exist.
 * TODO: Only for test pusposed, eventually will be removed.
 */
@Controller
public class InitController extends AbstractController {

    @RequestMapping(value = "/init")
    public String hello() {

        /** Adding initial admin user, will be changed later on */
        if (getUserFacade().getUserByUsername("admin") != null) {
            final UserDTO userDTO = new UserDTO();
            userDTO.setUsername("admin");
            userDTO.setPassword(getPasswordEncoder().encode("nimda"));
            userDTO.setRole(AuthorityRoles.ADMIN);
            userDTO.setEnabled((byte) 1);
            getRegisterFacade().registerUser(userDTO);
        }

        return TEMPLATE_LOGIN_PAGE;
    }
}
