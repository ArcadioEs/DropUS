package org.engineer.work.service;

import org.engineer.work.model.UserGroups;

/**
 * Service managing relationship between {@link org.engineer.work.model.UserEntity} and {@link org.engineer.work.model.GroupEntity}.
 */
public interface UserGroupsService {

    UserGroups getUserGroupsByUsername(final String username);

    boolean createOrUpdateUserGroups(final String username, final String groupName);

    boolean updateUserGroups(final UserGroups user);

    boolean deleteUserGroups(final String username);
}
