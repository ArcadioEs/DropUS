package org.engineer.work.model;

import org.engineer.work.dto.UserDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Entity representing User in the system.
 */
@Entity
@Table(name = "users")
public class UserEntity {

	@Id
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false, columnDefinition="TEXT")
	private String password;
	@Column(nullable = false)
	private byte enabled;
	@Column(nullable = false)
	private String role;

	public UserEntity(final UserDTO userDTO) {
		this.setUsername(userDTO.getUsername());
		this.setPassword(userDTO.getPassword());
		this.setEnabled(userDTO.getEnabled());
		this.setRole(userDTO.getRole());
	}

	protected UserEntity() {
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public byte getEnabled() {
		return enabled;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
