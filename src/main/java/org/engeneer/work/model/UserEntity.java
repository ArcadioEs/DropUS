package org.engeneer.work.model;

import org.engeneer.work.utils.EncryptUtils;

import javax.persistence.*;


/**
 * Entity representing User in the system.
 */
@Entity
@Table(name = "users")
public class UserEntity {

	@Id
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false)
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
//		return EncryptUtils.base64decode(this.password);
	}

	public void setPassword(String password) {
//		this.password = EncryptUtils.base64encode(password);
		this.password = password;
	}
}
