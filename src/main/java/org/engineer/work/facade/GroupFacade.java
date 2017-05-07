package org.engineer.work.facade;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.exception.group.GroupExistsException;

import java.util.List;

/**
 * Facade for group management.
 */
public interface GroupFacade {

	GroupDTO getGroupByName(final String name);

	/**
	 * Creates group, throws exception if group exists.
	 */
	void createGroup(final GroupDTO groupDTO) throws GroupExistsException;

	List<GroupDTO> getAllGroups();
}
