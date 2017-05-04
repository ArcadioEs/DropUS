package org.engineer.work.facade;

import org.engineer.work.dto.GroupDTO;

import java.util.List;

/**
 * Facade for group management.
 */
public interface GroupFacade {

	GroupDTO getGroupByName(final String name);

	boolean createGroup(final GroupDTO groupDTO);

	List<GroupDTO> getAllGroups();
}
