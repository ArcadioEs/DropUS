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

    List<GroupDTO> getUserGroups(final String username);

    /**
     * If 'add' is true - method adds group to pending list.
     * If false - method removes group from pending list.
     */
    boolean updatePendingUsers(final String username, final String groupName, final boolean add);

    /**
     * If 'add' is true - method adds user to members list.
     * If false - method removes user from members list.
     */
    boolean updateGroupMembers(final String username, final String groupName, final boolean add);

    boolean deleteGroup(final String name);

    /**
     * Returns user role in given group.
     * @return
     * <ul>
     *  <li>isAdmin - if given user is admin of group</li>
     *  <li>isMember - if given user is member of group</li>
     *  <li>isPending - if user already wants to join group</li>
     *  <li>isNotPending - if user is not connected with group in any way</li>
     * </ul>
     */
    String determineUserRoleInGroup(final String username, final String groupName);
}
