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
        }
        return userGroups;
    }

    @Override
    @Transactional
    public boolean createUserGroups(final String username) {
        boolean result = false;
        if (username != null && !userGroupsRepository.exists(username)) {
            try {
                userGroupsRepository.save(new UserGroups(username));
                result = true;
            } catch (IllegalArgumentException e) {
                LOG.warn("Creating UserGroups entity with username {} failed", username, e);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean addGroup(final String username, final String groupName) {
        boolean result = false;
        if (username != null && groupName != null) {
            final UserGroups user = userGroupsRepository.findOne(username);

            if (user != null) {
                if (user.getGroups() == null) {
                    user.setGroups(Arrays.asList(groupName));
                    userGroupsRepository.save(user);
                    result = true;
                } else if (!user.getGroups().contains(groupName)) {
                    user.getGroups().add(groupName);
                    userGroupsRepository.save(user);
                    result = true;
                }
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean updateUserGroups(final UserGroups user) {
        boolean result = true;
        if (user != null
                && user.getUsername() != null
                && userGroupsRepository.exists(user.getUsername())) {

            userGroupsRepository.save(user);
            result = true;
        }
        return result;
    }
}
