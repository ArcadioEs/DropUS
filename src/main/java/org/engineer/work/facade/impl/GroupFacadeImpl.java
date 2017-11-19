package org.engineer.work.facade.impl;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.facade.GroupFacade;
import org.engineer.work.facade.UserFacade;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.bounding.UserGroups;
import org.engineer.work.model.UserEntity;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.UserGroupsService;
import org.engineer.work.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(GroupFacadeImpl.class);

    @Resource
    private GroupService groupService;
    @Resource
    private UserService userService;
    @Resource
    private UserFacade userFacade;
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
        } else {
            LOG.warn("Finding UserGroups failed. Username is null");
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

            if (add) {
                result = this.addUserToPendingGroup(user, group);
            } else {
                result = this.removeUserFromPendingGroup(user, group);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean updateGroupMembers(final String username, final String groupName, final boolean add) {
        return groupService.updateGroupMembers(username, groupName, add);
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
    private GroupDTO convertEntityToDTO(final GroupEntity groupEntity) {
        GroupDTO groupDTO = null;
        if (groupEntity != null) {
            groupDTO = new GroupDTO();

            groupDTO.setName(groupEntity.getName());
            groupDTO.setGroupOwner(groupEntity.getGroupOwner());
            groupDTO.setDescription(groupEntity.getDescription());
            groupDTO.setMembers(groupEntity.getMembers());
            groupDTO.setPendingUsers(groupEntity.getPendingUsers());
        } else {
            LOG.warn("Group entity is null, therefore cannot be converted");
        }
        return groupDTO;
    }

    private boolean addUserToPendingGroup(final UserEntity user, final GroupEntity group) {
        boolean result = false;
        if (user != null && group != null) {
            final UserGroups userGroups = userGroupsService.getUserGroupsByUsername(user.getUsername());
            final List<String> groupsPending = group.getPendingUsers();

            if (!groupsPending.contains(user.getUsername())
                    && (userGroups == null || !userGroups.getGroups().contains(group.getName()))) {

                groupsPending.add(user.getUsername());
                result = true;
            } else {
                LOG.warn("User {} could not be added to pending group {}", user.getUsername(), group.getName());
            }
        } else {
            LOG.warn("User and group must not be null");
        }
        return result;
    }

    private boolean removeUserFromPendingGroup(final UserEntity user, final GroupEntity group) {
        boolean result = false;
        if (user != null && group != null) {
            final UserGroups userGroups = userGroupsService.getUserGroupsByUsername(user.getUsername());
            final List<String> groupsPending = group.getPendingUsers();

            if (groupsPending != null
                    && groupsPending.contains(user.getUsername())
                    && (userGroups == null || !userGroups.getGroups().contains(group.getName()))) {

                groupsPending.remove(user.getUsername());
                result = true;
            } else {
                LOG.warn("User {} could not be removed from pending group {}", user.getUsername(), group.getName());
            }
        } else {
            LOG.warn("User and group must not be null");
        }
        return result;
    }
}
