package org.engineer.work.service.impl;

import com.google.common.base.Preconditions;
import org.engineer.work.dto.GroupDTO;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.UserGroups;
import org.engineer.work.repository.GroupRepository;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.UserGroupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    public boolean addMemberToGroup(final String username, final String groupName) {
        boolean result = false;
        if (username != null && groupName != null) {
            final GroupEntity groupToAdd = this.getGroupByName(groupName);
            final UserGroups user = userGroupsService.getUserGroupsByUsername(username);

            if (this.validateIfMemberCanBeAddedToGroup(groupToAdd, user)) {
                groupToAdd.getPendingUsers().remove(username);

                if (groupToAdd.getMembers() != null) {
                    groupToAdd.getMembers().add(username);
                } else {
                    groupToAdd.setMembers(asList(username));
                }
                groupRepository.save(groupToAdd);
                userGroupsService.addGroup(user.getUsername(), groupToAdd.getName());

                result = true;
            }
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

                result = true;
            } catch (IllegalArgumentException e) {
                LOG.warn("Creating group with name {} failed", groupDTO.getName(), e);
            }
        }
        return result;
    }

    @Override
    public boolean updateGroup(final GroupEntity groupEntity) {
        boolean result = false;
        if (groupEntity != null
                && groupEntity.getName() != null
                && groupRepository.exists(groupEntity.getName())) {

            groupRepository.save(groupEntity);
            result = true;
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

            final List<String> groupMembers = this.getGroupByName(name).getMembers();
            if (groupMembers != null) {
                for (final String member : groupMembers) {
                    final UserGroups user = userGroupsService.getUserGroupsByUsername(member);
                    if (user != null) {
                        user.getGroups().removeIf(group -> name.equals(group));
                        userGroupsService.updateUserGroups(user);
                    }
                }
            }
            groupRepository.delete(name);
            result = true;
        }
        return result;
    }

    private boolean validateIfMemberCanBeAddedToGroup(final GroupEntity groupEntity, final UserGroups user) {
        Preconditions.checkArgument(groupEntity != null);
        Preconditions.checkArgument(user != null);
        Preconditions.checkArgument(groupEntity.getPendingUsers() != null);

        return (groupEntity.getPendingUsers().contains(user.getUsername()) && !groupEntity.getMembers().contains(user.getUsername()) && !user.getGroups().contains(groupEntity.getName()));
    }
}
