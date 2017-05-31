package org.engineer.work.service;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.model.GroupEntity;

import java.util.List;

/**
 * Service managing groups in the system.
 */
public interface GroupService {

    GroupEntity getGroupByName(final String name);

    boolean updateGroupMembers(final String username, final String groupName, final boolean add);

    boolean createGroup(final GroupDTO groupDTO);

    boolean updateGroup(final GroupEntity groupEntity);

    List<GroupEntity> getAllGroups();

    boolean deleteGroup(final String name);
}
