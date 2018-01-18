package org.engineer.work.service.impl;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.PostEntity;
import org.engineer.work.model.UserEntity;
import org.engineer.work.model.bounding.UserGroups;
import org.engineer.work.repository.GroupRepository;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.PostService;
import org.engineer.work.service.UserGroupsService;
import org.engineer.work.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;

/**
 * Default implementation of {@link GroupService}.
 */
@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Resource
    private GroupRepository groupRepository;
    @Resource
    private UserService userService;
    @Resource
    private PostService postService;
    @Resource
    private UserGroupsService userGroupsService;

    @Override
    public GroupEntity getGroupByName(final String name) {
        GroupEntity groupEntity = null;
        if (name != null) {
            groupEntity = groupRepository.findOne(name);
        }
        return groupEntity;
    }

    @Override
    @Transactional
    public boolean updateGroupMembers(final String username, final String groupName, final boolean add) {
        boolean result = false;
        if (username != null && groupName != null) {
            final UserEntity user = userService.getUserByUsername(username);
            final GroupEntity group = this.getGroupByName(groupName);

            final boolean condition = this.validateIfGroupCanBeUpdated(user, group, add);
            if (add && condition) {
                group.getPendingUsers().remove(username);
                group.getMembers().add(username);

                result = userGroupsService.createOrUpdateUserGroups(user.getUsername(), group.getName(), TRUE);
            } else if (!add && condition) {
                group.getMembers().remove(user.getUsername());

                result = userGroupsService.createOrUpdateUserGroups(user.getUsername(), group.getName(), FALSE);
            }
        } else {
            LOG.warn("Username and group name must not be null");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean createGroup(final GroupDTO groupDTO) {
        boolean result = false;
        if (groupDTO != null
                && groupDTO.getName() != null
                && !groupRepository.exists(groupDTO.getName())) {

            try {
                final GroupEntity groupEntity = new GroupEntity(groupDTO);
                groupEntity.setMembers(asList(groupDTO.getGroupOwner()));

                groupRepository.save(groupEntity);
                result = userGroupsService.createOrUpdateUserGroups(groupDTO.getGroupOwner(), groupDTO.getName(), TRUE);
            } catch (IllegalArgumentException e) {
                LOG.warn("Creating group with name {} failed", groupDTO.getName(), e);
            }
        } else {
            if (groupDTO == null) {
                LOG.warn("Creating group failed - DTO is null");
            } else {
                LOG.warn("Creating group {} failed", groupDTO.getName());
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean updateGroup(final GroupEntity groupEntity) {
        boolean result = false;
        if (groupEntity != null
                && groupEntity.getName() != null
                && groupRepository.exists(groupEntity.getName())) {

            groupRepository.save(groupEntity);
            result = true;
        } else {
            if (groupEntity == null) {
                LOG.warn("Could not update given group - group is null");
            } else {
                LOG.warn("Could not update group {}", groupEntity.getName());
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean updatePosts(final String groupName, final Long postID, final boolean add) {
        boolean result = false;
        if (groupName != null) {
            if (add) {
                result = this.addPost(groupName, postID);
            } else {
                result = this.removePost(groupName, postID);
            }
        }
        return result;
    }

    private boolean addPost(final String groupName, final Long postID) {
        boolean result = false;
        if (groupName != null && groupRepository.exists(groupName)) {
            final GroupEntity group = this.getGroupByName(groupName);
            final PostEntity post = postService.findPost(postID);
            if (group.getPosts().isEmpty() || !group.getPosts().contains(post)) {
                group.getPosts().add(post);
                result = true;
            }
        }
        return result;
    }

    private boolean removePost(final String groupName, final Long postID) {
        boolean result = false;
        if (groupName != null
                && groupRepository.exists(groupName)
                && postService.findPost(postID) != null) {
            final GroupEntity group = this.getGroupByName(groupName);
            final PostEntity post = postService.findPost(postID);
            if (group.getPosts().contains(post)) {
                group.getPosts().remove(post);
                result = true;
            }
        }
        return result;
    }

    @Override
    public List<GroupEntity> getAllGroups() {
        final List<GroupEntity> list = new ArrayList<>();
        groupRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    @Override
    @Transactional
    public boolean deleteGroup(final String name) {
        boolean result = false;
        if (name != null && groupRepository.exists(name)) {
            this.deleteGroupMembers(name, this.getGroupByName(name).getMembers());
            this.deleteGroupPosts(this.getGroupByName(name).getPosts());
            groupRepository.delete(name);
            result = true;
        } else {
            LOG.warn("Group {} could not be deleted", name);
        }
        return result;
    }

    private void deleteGroupMembers(@NotNull final String groupName, @NotNull final List<String> groupMembers) {
        for (final String member : groupMembers) {
            final UserGroups user = userGroupsService.getUserGroupsByUsername(member);
            if (user != null) {
                user.getGroups().removeIf(group -> groupName.equals(group));
                userGroupsService.updateUserGroups(user);
            }
        }
    }

    private void deleteGroupPosts(@NotNull final List<PostEntity> postsToDelete) {
        for (final PostEntity post : postsToDelete) {
            postService.deletePost(post.getId());
        }
    }

    /**
     * Validates whether specified user can be added or removed from given group.
     * @param add flag which determines whether user should be added to group or removed
     */
    private boolean validateIfGroupCanBeUpdated(final UserEntity user, final GroupEntity group, final boolean add) {
        boolean result = false;
        if (user != null && group != null) {
            final UserGroups userGroups = userGroupsService.getUserGroupsByUsername(user.getUsername());
            if (add) {
                if (userGroups == null || !userGroups.getGroups().contains(group.getName())) {
                    result = group.getPendingUsers().contains(user.getUsername()) && !group.getMembers().contains(user.getUsername());
                } else {
                    LOG.warn("User {} could not be added to group {}", user.getUsername(), group.getName());
                }
            } else {
                if (userGroups != null && userGroups.getGroups().contains(group.getName()) && group.getMembers().contains(user.getUsername())) {
                    result = true;
                } else {
                    LOG.warn("User {} could not be removed from group {}", user.getUsername(), group.getName());
                }
            }
        } else {
            LOG.warn("User and group must not be null");
        }
        return result;
    }
}
