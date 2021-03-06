package org.engineer.work.model.bounding;

import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.UserEntity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing relationship between {@link UserEntity} and {@link GroupEntity}.
 */
@Entity
@Table(name = "usergroups")
public class UserGroups {

    @Id
    @Column(nullable = false, unique = true)
    private String username;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> groups = new ArrayList<>();

    public UserGroups(final String username) {
        this.setUsername(username);
    }

    protected UserGroups() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        if (username != null) {
            this.username = username;
        } else {
            throw new IllegalArgumentException("Username must not be null");
        }
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(final List<String> groups) {
        this.groups = groups;
    }
}
