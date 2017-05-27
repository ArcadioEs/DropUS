package org.engineer.work.facade.impl;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.facade.RegisterFacade;
import org.engineer.work.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RegisterFacadeImpl implements RegisterFacade {

    @Resource
    private UserService userService;

    @Override
    public boolean registerUser(final UserDTO userDTO) {
        return userService.createUser(userDTO);
    }
}
