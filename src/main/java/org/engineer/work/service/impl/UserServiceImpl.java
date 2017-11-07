package org.engineer.work.service.impl;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.UserEntity;
import org.engineer.work.repository.UserRepository;
import org.engineer.work.service.PropertiesService;
import org.engineer.work.service.UserGroupsService;
import org.engineer.work.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link UserService}.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String FILES_LOCATION = "dropus.files.root";
    private static final String SHARED = "/shared/";
    private static final String NOT_SHARED = "/not_shared/";

    @Resource
    private UserRepository userRepository;
    @Resource
    private UserGroupsService userGroupsService;
    @Resource
    private PropertiesService propertiesService;

    @Override
    public UserEntity getUserByUsername(final String username) {
        UserEntity userEntity = null;
        if (username != null) {
            userEntity = userRepository.findOne(username);
        }
        return userEntity;
    }

    @Override
    @Transactional
    public boolean createUser(final UserDTO userDTO) {
        boolean result = false;
        if (userDTO != null
                && userDTO.getUsername() != null
                && !userRepository.exists(userDTO.getUsername())) {

            try {
                userRepository.save(new UserEntity(userDTO));
                this.createUserFolder(userDTO.getUsername());
                result = true;
            } catch (IllegalArgumentException e) {
                LOG.warn("Creating user with username {} failed", userDTO.getUsername(), e);
            }
        } else {
            if (userDTO == null) {
                LOG.warn("User could not be created - DTO is null");
            } else {
                LOG.warn("User {} could not be created", userDTO.getUsername());
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean updateUser(final UserEntity userEntity) {
        boolean result = false;
        if (userEntity != null
                && userEntity.getUsername() != null
                && userRepository.exists(userEntity.getUsername())) {

            userRepository.save(userEntity);
            result = true;
        } else {
            if (userEntity == null) {
                LOG.warn("User could not be updated - entity is null");
            } else {
                LOG.warn("User {} could not be updated", userEntity.getUsername());
            }
        }
        return result;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        final List<UserEntity> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }


    @Override
    @Transactional
    public boolean deleteUser(final String username) {
        boolean result = false;
        if (username != null && userRepository.exists(username)) {
            userRepository.delete(username);
            userGroupsService.deleteUserGroups(username);
            result = true;
        } else {
            LOG.warn("User {} could not be deleted", username);
        }
        return result;
    }

    private void createUserFolder(final String username) {
        final String rootLocation = propertiesService.getProperty(FILES_LOCATION);
        final String pathShared = rootLocation + username + SHARED;
        final String pathNotShared = rootLocation + username + NOT_SHARED;

        if (! new File(pathShared).mkdirs()) {
            LOG.warn("Path ({}) could not be created", pathShared);
        }
        if (! new File(pathNotShared).mkdirs()) {
            LOG.warn("Path ({}) could not be created", pathNotShared);
        }
    }
}
