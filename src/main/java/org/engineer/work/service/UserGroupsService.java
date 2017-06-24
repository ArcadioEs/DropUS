package org.engineer.work.service;

import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.UserEntity;
import org.engineer.work.model.bounding.UserGroups;

/**
 * Service managing relationship between {@link UserEntity} and {@link GroupEntity}.
 */
public interface UserGroupsService {

    UserGroups getUserGroupsByUsername(final String username);

    /**
     * If 'add' is true - method adds user to members list.
     * If false - method removes user from members list.
     */
    boolean createOrUpdateUserGroups(final String username, final String groupName, final boolean add);

    boolean updateUserGroups(final UserGroups user);

    boolean deleteUserGroups(final String username);
}
