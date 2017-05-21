package org.engineer.work.dto;

import java.util.List;

/**
 * Group data transfer object.
 */
public class GroupDTO {

    private String name;
    private String groupOwner;
    private String description;
    private List<String> users;
    private List<String> pendings;

    public List<String> getPendings() {
        return pendings;
    }

    public void setPendings(List<String> pendings) {
        this.pendings = pendings;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
