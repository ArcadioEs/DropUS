package org.engineer.work.service;

import org.engineer.work.model.bounding.UserGroups;

/**
 * Service managing relationship between {@link org.engineer.work.model.UserEntity} and {@link org.engineer.work.model.GroupEntity}.
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
