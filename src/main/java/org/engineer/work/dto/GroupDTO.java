package org.engineer.work.dto;

import java.util.Set;

/**
 * Group data transfer object.
 */
public class GroupDTO {

	private String name;
	private String groupOwner;
	private Set<UserDTO> users;

	public Set<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(Set<UserDTO> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupOwner() {
		return groupOwner;
	}

	public void setGroupOwner(String groupOwner) {
		this.groupOwner = groupOwner;
	}
}
