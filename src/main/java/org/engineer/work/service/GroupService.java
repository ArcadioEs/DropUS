package org.engineer.work.service;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.model.GroupEntity;

import java.util.List;

/**
 * Service managing groups in the system.
 */
public interface GroupService {

	GroupEntity getGroupByName(final String name);

	/**
	 * Updates group's member list. If add is true - it tries to add user to given group, if false - tries to remove user from group.
	 * Returns true when, and only when operation was successful.
	 */
	boolean updateGroupMembers(final String username, final String groupName, final boolean add);

	boolean createGroup(final GroupDTO groupDTO);

	boolean updateGroup(final GroupEntity groupEntity);

	/**
	 * Updates posts in given group.
	 * If 'add' is true - post will be added to group's posts, if false - post will be removed.
	 */
	boolean updatePosts(final String groupName, final Long postID, final boolean add);

	List<GroupEntity> getAllGroups();

	boolean deleteGroup(final String name);
}
