package org.engineer.work.facade.impl;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.facade.UserFacade;
import org.engineer.work.model.UserEntity;
import org.engineer.work.model.bounding.UserGroups;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.engineer.work.service.UserGroupsService;
import org.engineer.work.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of UserFacade.
 */
@Service
public class UserFacadeImpl implements UserFacade {

    private static final Logger LOG = LoggerFactory.getLogger(UserFacadeImpl.class);

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
    public boolean createUser(final UserDTO userDTO) {
        return userService.createUser(userDTO);
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
            } else {
                LOG.warn("User {} could not be {}, since user is null", username, enabled ? "enabled" : "disabled");
            }
        } else {
            LOG.warn("Could not update user's enabled status - username is null");
        }
    }

    @Override
    public boolean deleteUser(final String username) {
        return userService.deleteUser(username);
    }

    /**
     * Converts given entity into DTO.
     *
     * @param userEntity entity received from service layer
     * @return properly prepared DTO
     */
    private UserDTO convertEntityToDTO(final UserEntity userEntity) {
        UserDTO userDTO = null;
        if (userEntity != null) {
            userDTO = new UserDTO();

            userDTO.setUsername(userEntity.getUsername());
            userDTO.setEnabled(userEntity.getEnabled());
            final UserGroups userGroups = userGroupsService.getUserGroupsByUsername(userEntity.getUsername());
            if (userGroups != null) {
                userDTO.setUserGroups(userGroups.getGroups());
            }
        } else {
            LOG.warn("User entity is null, therefore cannot be converted to DTO");
        }
        return userDTO;
    }
}
