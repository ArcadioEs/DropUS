package org.engineer.work.facade.impl;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.facade.GroupFacade;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.UserEntity;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link GroupFacade} interface.
 */
@Service
public class GroupFacadeImpl implements GroupFacade {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

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
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups().stream().map(group -> convertEntityToDTO(group)).collect(Collectors.toList());
    }

    @Override
    public List<GroupDTO> getUserGroups(final String username) {
        List<GroupDTO> userGroups = null;
        if (username != null) {
            final UserEntity user = userService.getUserByUsername(username);
            if (user != null && user.getGroups() != null) {
                userGroups = user.getGroups().stream().map(group -> convertEntityToDTO(group)).collect(Collectors.toList());
            }
        }
        return userGroups;
    }

    @Override
    public boolean updatePendingUsers(final String username, final String groupName, final boolean add) {
        boolean result = false;
        if (username != null && groupName != null) {
            final UserEntity user = userService.getUserByUsername(username);
            final GroupEntity group = groupService.getGroupByName(groupName);

            if (user != null
                    && user.getGroupsPending() != null
                    && group != null) {
                final List<GroupEntity> groupsPending = user.getGroupsPending();
                if (add && !user.getGroupsPending().contains(group)) {
                    groupsPending.add(group);
                    userService.updateUser(user);
                    result = true;
                }
                if (!add && user.getGroupsPending().contains(group)) {
                    groupsPending.remove(group);
                    userService.updateUser(user);
                    result = true;
                }
            }
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
            if (groupEntity.getUsers() != null) {
                groupDTO.setUsers(groupEntity.getUsers().stream().map(user -> user.getUsername()).collect(Collectors.toList()));
            }
            if (groupEntity.getUsersPending() != null) {
                groupDTO.setPendings(groupEntity.getUsersPending().stream().map(user -> user.getUsername()).collect(Collectors.toList()));
            }
        }
        return groupDTO;
    }
}
