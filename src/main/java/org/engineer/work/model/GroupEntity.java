package org.engineer.work.model;

import org.engineer.work.dto.GroupDTO;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
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
    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(max = 255)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "groups")
    private List<UserEntity> users;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "groupsPending")
    private List<UserEntity> usersPending;

    public GroupEntity(final GroupDTO groupDTO) throws IllegalArgumentException {
        this.setName(groupDTO.getName());
        this.setGroupOwner(groupDTO.getGroupOwner());
        this.setDescription(groupDTO.getDescription());
    }

    protected GroupEntity() {
    }

    public List<UserEntity> getUsersPending() {
        return usersPending;
    }

    public void setUsersPending(final List<UserEntity> usersPending) {
        this.usersPending = usersPending;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(final List<UserEntity> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Group name must not be null");
        }
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(final String groupOwner) {
        if (groupOwner != null) {
            this.groupOwner = groupOwner;
        } else {
            throw new IllegalArgumentException("Group owner must not be null");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        if (description != null) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("Description must not be null nor empty");
        }
    }
}
