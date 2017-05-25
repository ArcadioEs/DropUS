package org.engineer.work.dto;

import java.util.List;

/**
 * Group data transfer object.
 */
public class GroupDTO {

    private String name;
    private String groupOwner;
    private String description;
    private List<String> members;
    private List<String> pendingUsers;

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(final List<String> members) {
        this.members = members;
    }

    public List<String> getPendingUsers() {
        return pendingUsers;
    }

    public void setPendingUsers(final List<String> pendingUsers) {
        this.pendingUsers = pendingUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(final String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
