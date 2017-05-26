package org.engineer.work.facade.impl;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.facade.UserFacade;
import org.engineer.work.model.UserEntity;
import org.engineer.work.model.UserGroups;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.engineer.work.service.UserGroupsService;
import org.engineer.work.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of UserFacade.
 */
@Service
public class UserFacadeImpl implements UserFacade {

    @Resource
    private UserService userService;
    @Resource
    private UserGroupsService userGroupsService;

    @Override
    public boolean userIsAdmin(final String username) {
        boolean result = false;
        if (username != null && userService.getUserByUsername(username) != null) {
            if (AuthorityRoles.ADMIN.equals(userService.getUserByUsername(username).getRole())) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public UserDTO getUserByUsername(final String username) {
        return this.convertEntityToDTO(userService.getUserByUsername(username));
    }

    @Override
    public boolean userExists(final String username) {
        boolean result = false;
        if (username != null && userService.getUserByUsername(username) != null) {
            result = true;
        }
        return result;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream().map(userEntity -> this.convertEntityToDTO(userEntity)).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getRegularUsers() {
        return this.getAllUsers().stream().filter(user -> !this.userIsAdmin(user.getUsername())).collect(Collectors.toList());
    }

    @Override
    public void updateUserEnabledStatus(final String username, final boolean enabled) {
        if (username != null) {
            final UserEntity user = userService.getUserByUsername(username);

            if (user != null) {
                user.setEnabled(enabled ? (byte) 1 : (byte) 0);
                userService.updateUser(user);
            }
        }
    }

    /**
     * Converts given entity into DTO.
     *
     * @param userEntity entity received from service layer
     * @return properly prepared DTO
     */
    public UserDTO convertEntityToDTO(final UserEntity userEntity) {
        UserDTO userDTO = null;
        if (userEntity != null) {
            userDTO = new UserDTO();

            userDTO.setUsername(userEntity.getUsername());
            userDTO.setEnabled(userEntity.getEnabled());
            final UserGroups userGroups = userGroupsService.getUserGroupsByUsername(userEntity.getUsername());
            if (userGroups != null) {
                userDTO.setUserGroups(userGroups.getGroups());
            }
        }
        return userDTO;
    }
}
