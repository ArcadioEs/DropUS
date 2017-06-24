package org.engineer.work.service;

import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.PostEntity;
import org.engineer.work.model.bounding.GroupPosts;

/**
 * Service managing relationship between {@link GroupEntity} and {@link PostEntity}.
 */
public interface GroupPostsService {

    GroupPosts getGroupPostsByGroupName(final String groupName);

    /**
     * If 'add' is true - method adds post the group.
     * If false - method removes post from the group.
     */
    boolean createOrUpdateGroupPosts(final String groupName, final Long postID, final boolean add);

    boolean updateGroupPosts(final GroupPosts groupPosts);

    boolean deleteGroupPosts(final String groupName);
}
