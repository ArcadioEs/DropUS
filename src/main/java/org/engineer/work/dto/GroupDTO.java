package org.engineer.work.dto;

/**
 * Group data transfer object.
 */
public class GroupDTO {

	private String name;
	private String groupOwner;

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
