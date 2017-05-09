package org.engineer.work.service;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.model.GroupEntity;

import java.util.List;

/**
 * Service managing groups in the system.
 */
public interface GroupService {

	GroupEntity getGroupByName(final String name);

	void updateGroupMembers(final String user, final String group);

	boolean createGroup(final GroupDTO groupDTO);

	List<GroupEntity> getAllGroups();

	boolean deleteGroup(final String name);
}
