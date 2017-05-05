package org.engineer.work.model;

import org.engineer.work.dto.GroupDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Entity representing Group in the system.
 */
@Entity
@Table(name = "groups")
public class GroupEntity {

	@Id
	@Column(nullable = false, unique = true)
	private String name;
	@Column(nullable = false)
	private String groupOwner;
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "groups")
	private List<UserEntity> users;

	public GroupEntity(final GroupDTO groupDTO) {
		this.setName(groupDTO.getName());
		this.setGroupOwner(groupDTO.getGroupOwner());
	}

	protected GroupEntity() {
	}

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
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
