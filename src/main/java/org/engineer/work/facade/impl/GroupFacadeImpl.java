package org.engineer.work.facade.impl;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.exception.group.GroupExistsException;
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
	public void createGroup(final GroupDTO groupDTO) throws GroupExistsException {
		groupService.createGroup(groupDTO);
	}

	@Override
	public List<GroupDTO> getAllGroups() {
		return groupService.getAllGroups().stream().map(group -> convertEntityToDTO(group)).collect(Collectors.toList());
	}

	/**
	 * Converts given entity into DTO.
	 * @param groupEntity entity received from service layer
	 * @return properly prepared DTO
	 */
	protected GroupDTO convertEntityToDTO(final GroupEntity groupEntity) {
		final GroupDTO groupDTO = new GroupDTO();

		groupDTO.setName(groupEntity.getName());
		groupDTO.setGroupOwner(groupEntity.getGroupOwner());

		return groupDTO;
	}
}
