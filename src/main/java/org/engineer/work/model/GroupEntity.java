package org.engineer.work.model;

import org.engineer.work.dto.GroupDTO;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> members;
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> pendingUsers;
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<PostEntity> posts;

    public GroupEntity(final GroupDTO groupDTO) throws IllegalArgumentException {
        this.setName(groupDTO.getName());
        this.setGroupOwner(groupDTO.getGroupOwner());
        this.setDescription(groupDTO.getDescription());
    }

    protected GroupEntity() {
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<String> getPendingUsers() {
        return pendingUsers;
    }

    public void setPendingUsers(List<String> pendingUsers) {
        this.pendingUsers = pendingUsers;
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

    public List<PostEntity> getPosts() {
        return posts;
    }

    public void setPosts(final List<PostEntity> posts) {
        this.posts = posts;
    }
}
