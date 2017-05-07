package org.engineer.work.service;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.exception.group.GroupExistsException;
import org.engineer.work.exception.group.GroupNotFoundException;
import org.engineer.work.model.GroupEntity;

import java.util.List;

/**
 * Service managing groups in the system.
 */
public interface GroupService {

	GroupEntity getGroupByName(final String name);

	/**
	 * Creates group, throws exception if group already exists.
	 */
	void createGroup(final GroupDTO groupDTO) throws GroupExistsException;

	List<GroupEntity> getAllGroups();

	/**
	 * Deletes group, throws exception if group could not be found.
	 */
	void deleteGroup(final String name) throws GroupNotFoundException;
}
