package org.engineer.work.dto;

import java.util.Set;

/**
 * User data transfer object.
 */
public class UserDTO {

	private String username;
	private String password;
	private byte enabled;
	private String role;
	private Set<GroupDTO> groups;

	public Set<GroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(Set<GroupDTO> groups) {
		this.groups = groups;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte getEnabled() {
		return enabled;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}
}
