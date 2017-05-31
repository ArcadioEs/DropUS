package org.engineer.work.service.impl;

import org.engineer.work.model.UserGroups;
import org.engineer.work.repository.UserGroupsRepository;
import org.engineer.work.service.UserGroupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;

/**
 * Default implementation of {@link UserGroupsService}.
 */
@Service
public class UserGroupsServiceImpl implements UserGroupsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserGroupsServiceImpl.class);

    @Resource
    private UserGroupsRepository userGroupsRepository;

    @Override
    public UserGroups getUserGroupsByUsername(final String username) {
        UserGroups userGroups = null;
        if (username != null) {
            userGroups = userGroupsRepository.findOne(username);
            if (userGroups != null && userGroups.getGroups() == null) {
                userGroups.setGroups(Collections.emptyList());
            }
        }
        return userGroups;
    }

    @Override
    @Transactional
    public boolean createOrUpdateUserGroups(final String username, final String groupName, final boolean add) {
        boolean result = false;
        if (username != null) {
            try {
                UserGroups userGroups = this.getUserGroupsByUsername(username);
                if (add && userGroups == null) {
                    userGroups = new UserGroups(username);
                    userGroups.setGroups(Arrays.asList(groupName));
                    userGroupsRepository.save(userGroups);
                    result = true;
                } else if (add) {
                    userGroups.getGroups().add(groupName);
                    result = this.updateUserGroups(userGroups);
                } else if (userGroups != null) {
                    userGroups.getGroups().remove(groupName);
                    result = this.updateUserGroups(userGroups);
                }
            } catch (IllegalArgumentException e) {
                LOG.warn("Creating UserGroups entity with username {} failed", username, e);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean updateUserGroups(final UserGroups user) {
        boolean result = false;
        if (user != null
                && user.getUsername() != null
                && userGroupsRepository.exists(user.getUsername())) {

            userGroupsRepository.save(user);
            result = true;
        } else {
            if (user == null) {
                LOG.warn("Could not update given UserGroup - user is null");
            } else {
                LOG.warn("Could not update UserGroup for user {}", user.getUsername());
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteUserGroups(final String username) {
        boolean result = false;
        if (username != null && userGroupsRepository.exists(username)) {
            userGroupsRepository.delete(username);
            result = true;
        } else {
            LOG.warn("UserGroups {} could not be deleted", username);
        }
        return result;
    }
}
