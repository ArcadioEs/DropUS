package org.engineer.work.facade.impl;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.facade.GroupFacade;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.UserEntity;
import org.engineer.work.model.UserGroups;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.UserGroupsService;
import org.engineer.work.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link GroupFacade} interface.
 */
@Service
public class GroupFacadeImpl implements GroupFacade {

    @Resource
    private GroupService groupService;
    @Resource
    private UserService userService;
    @Resource
    private UserGroupsService userGroupsService;

    @Override
    public GroupDTO getGroupByName(final String name) {
        return convertEntityToDTO(groupService.getGroupByName(name));
    }

    @Override
    public boolean groupExists(final String name) {
        boolean result = false;
        if (name != null && groupService.getGroupByName(name) != null) {
            result = true;
        }
        return result;
    }

    @Override
    public boolean createGroup(final GroupDTO groupDTO) {
        return groupService.createGroup(groupDTO);
    }

    @Override
    @Transactional
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups().stream().map(group -> convertEntityToDTO(group)).collect(Collectors.toList());
    }

    @Override
    public List<GroupDTO> getUserGroups(final String username) {
        List<GroupDTO> userGroups = null;
        if (username != null) {
            final UserGroups user = userGroupsService.getUserGroupsByUsername(username);
            if (user != null && !user.getGroups().isEmpty()) {
                userGroups = user.getGroups().stream().map(group -> this.getGroupByName(group)).collect(Collectors.toList());
            }
        }
        return userGroups;
    }

    @Override
    @Transactional
    public boolean updatePendingUsers(final String username, final String groupName, final boolean add) {
        boolean result = false;
        if (username != null && groupName != null) {
            final UserEntity user = userService.getUserByUsername(username);
            final GroupEntity group = groupService.getGroupByName(groupName);

            if (user != null && group != null) {
                final UserGroups userToAddGroups = userGroupsService.getUserGroupsByUsername(user.getUsername());
                final List<String> groupsPending = group.getPendingUsers();

                if (add && !groupsPending.contains(user.getUsername())) {
                    if (userToAddGroups == null || !userToAddGroups.getGroups().contains(group.getName())) {
                        groupsPending.add(user.getUsername());
                        groupService.updateGroup(group);
                        result = true;
                    }
                }

                if (!add && groupsPending != null && groupsPending.contains(user.getUsername())) {
                    if (userToAddGroups == null || !userToAddGroups.getGroups().contains(group.getName())) {
                        groupsPending.remove(user.getUsername());
                        groupService.updateGroup(group);
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean updateGroupMember(final String username, final String groupName) {
        boolean result = false;
        if (username != null && groupName != null) {
            result = groupService.addMemberToGroup(username, groupName);
        }
        return result;
    }

    @Override
    public boolean deleteGroup(final String name) {
        return groupService.deleteGroup(name);
    }

    /**
     * Converts given entity into DTO.
     *
     * @param groupEntity entity received from service layer
     * @return properly prepared DTO
     */
    public GroupDTO convertEntityToDTO(final GroupEntity groupEntity) {
        GroupDTO groupDTO = null;
        if (groupEntity != null) {
            groupDTO = new GroupDTO();

            groupDTO.setName(groupEntity.getName());
            groupDTO.setGroupOwner(groupEntity.getGroupOwner());
            groupDTO.setDescription(groupEntity.getDescription());
            if (groupEntity.getMembers() != null) {
                groupDTO.setMembers(groupEntity.getMembers());
            }
            if (groupEntity.getPendingUsers() != null) {
                groupDTO.setPendingUsers(groupEntity.getPendingUsers());
            }
        }
        return groupDTO;
    }
}
