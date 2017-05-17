package org.engineer.work.service.impl;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.UserEntity;
import org.engineer.work.repository.GroupRepository;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of GroupService.
 */
@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

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
    public boolean updateGroupMembers(final String username, final String groupName) {
        boolean result = false;
        if (username != null && groupName != null) {

            final UserEntity userEntity = userService.getUserByUsername(username);
            final GroupEntity groupEntity = this.getGroupByName(groupName);

            if (userEntity != null && groupEntity != null) {
                final List<GroupEntity> userGroups = userEntity.getGroups();

                if (!userGroups.contains(this.getGroupByName(groupName))) {
                    userGroups.add(groupEntity);

                    userEntity.setGroups(userGroups);
                    userService.updateUser(userEntity);

                    result = true;
                }
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
                groupRepository.save(new GroupEntity(groupDTO));
                if (this.updateGroupMembers(groupDTO.getGroupOwner(), groupDTO.getName())) {
                    result = true;
                } else {
                    this.deleteGroup(groupDTO.getName());
                }
            } catch (IllegalArgumentException e) {
                LOG.warn("Creating group with name {} failed", groupDTO.getName(), e);
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
            for (final UserEntity userEntity : this.getGroupByName(name).getUsers()) {
                userEntity.getGroups().removeIf(group -> group.getName().equals(name));
                userService.updateUser(userEntity);
            }
            groupRepository.delete(name);
            result = true;
        }
        return result;
    }
}
