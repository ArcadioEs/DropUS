package org.engineer.work.facade;

import org.engineer.work.dto.GroupDTO;

import java.util.List;

/**
 * Facade for group management.
 */
public interface GroupFacade {

	boolean groupExists(final String name);

	GroupDTO getGroupByName(final String name);

	boolean createGroup(final GroupDTO groupDTO);

	List<GroupDTO> getAllGroups();

	boolean deleteGroup(final String name);
}
