package org.engineer.work.service.impl;

import org.engineer.work.model.bounding.GroupPosts;
import org.engineer.work.repository.bounding.GroupPostsRepository;
import org.engineer.work.service.GroupPostsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;

/**
 * Default implementation of {@link GroupPostsService}.
 */
@Service
public class GroupPostsServiceImpl implements GroupPostsService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupPostsServiceImpl.class);

    @Resource
    private GroupPostsRepository groupPostsRepository;

    @Override
    public GroupPosts getGroupPostsByGroupName(final String groupName) {
        GroupPosts groupPosts = null;
        if (groupName != null) {
            groupPosts = groupPostsRepository.findOne(groupName);
            if (groupPosts != null && groupPosts.getPosts() == null) {
                groupPosts.setPosts(Collections.emptyList());
            }
        }
        return groupPosts;
    }

    @Override
    @Transactional
    public boolean createOrUpdateGroupPosts(final String groupName, final Long postID, final boolean add) {
        boolean result = false;
        if (groupName != null) {
            try {
                GroupPosts groupPosts = this.getGroupPostsByGroupName(groupName);
                if (add && groupPosts == null) {
                    groupPosts = new GroupPosts(groupName);
                    groupPosts.setPosts(Arrays.asList(postID));
                    groupPostsRepository.save(groupPosts);
                    result = true;
                } else if (add) {
                    groupPosts.getPosts().add(postID);
                    result = this.updateGroupPosts(groupPosts);
                } else if (groupPosts != null) {
                    groupPosts.getPosts().remove(postID);
                    result = this.updateGroupPosts(groupPosts);
                }
            } catch (IllegalArgumentException e) {
                LOG.warn("Creating GroupPosts entity with group name {} failed", groupName, e);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean updateGroupPosts(final GroupPosts groupPosts) {
        boolean result = false;
        if (groupPosts != null
                && groupPosts.getGroupName() != null
                && groupPostsRepository.exists(groupPosts.getGroupName())) {

            groupPostsRepository.save(groupPosts);
            result = true;
        } else {
            if (groupPosts == null) {
                LOG.warn("Could not update given GroupPosts - GroupPosts is null");
            } else {
                LOG.warn("Could not update GroupPosts for group {}", groupPosts.getGroupName());
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteGroupPosts(final String groupName) {
        boolean result = false;
        if (groupName != null && groupPostsRepository.exists(groupName)) {
            groupPostsRepository.delete(groupName);
            result = true;
        } else {
            LOG.warn("GroupPosts {} could not be deleted", groupName);
        }
        return result;
    }
}
