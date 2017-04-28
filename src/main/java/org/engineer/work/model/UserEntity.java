package org.engineer.work.model;

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

	public UserEntity(String username, String password) {
		setUsername(username);
		setPassword(password);
		this.enabled = 1;
	}

	protected UserEntity() {
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
