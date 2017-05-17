package org.engineer.work.facade.impl;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.facade.GroupFacade;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.service.GroupService;
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
    public boolean deleteGroup(final String name) {
        return groupService.deleteGroup(name);
    }

    /**
     * Converts given entity into DTO.
     *
     * @param groupEntity entity received from service layer
     * @return properly prepared DTO
     */
    @Override
    public GroupDTO convertEntityToDTO(final GroupEntity groupEntity) {
        GroupDTO groupDTO = null;
        if (groupEntity != null) {
            groupDTO = new GroupDTO();

            groupDTO.setName(groupEntity.getName());
            groupDTO.setGroupOwner(groupEntity.getGroupOwner());
            groupDTO.setUsers(groupEntity.getUsers().stream().map(user -> user.getUsername()).collect(Collectors.toList()));
        }
        return groupDTO;
    }
}
