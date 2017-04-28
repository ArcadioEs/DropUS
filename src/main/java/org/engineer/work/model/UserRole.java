package org.engineer.work.model;

import org.engineer.work.model.enumeration.AuthorityRoles;

import javax.persistence.*;
import java.util.Arrays;

/**
 *
 */
@Entity
@Table(name = "user_roles")
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int user_role_id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String role;

	public UserRole(String username, String role) {
		setUsername(username);
		setRole(role);
	}

	protected UserRole() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		if(Arrays.asList(AuthorityRoles.USER, AuthorityRoles.ADMIN).contains(role.toUpperCase())) {
			this.role = role.toUpperCase();
		}
	}
}
