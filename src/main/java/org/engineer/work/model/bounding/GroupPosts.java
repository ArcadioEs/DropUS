package org.engineer.work.model.bounding;

import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.PostEntity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Entity representing relationship between {@link GroupEntity} and {@link PostEntity}.
 */
@Entity
@Table(name = "groupposts")
public class GroupPosts {

    @Id
    @Column(nullable = false, unique = true)
    private String groupName;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> posts;

    public GroupPosts(final String groupName) throws IllegalArgumentException {
        this.setGroupName(groupName);
    }

    protected GroupPosts() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(final String groupName) {
        if (groupName != null) {
            this.groupName = groupName;
        } else {
            throw new IllegalArgumentException("Group name must not be null");
        }
    }

    public List<Long> getPosts() {
        return posts;
    }

    public void setPosts(final List<Long> posts) {
        this.posts = posts;
    }
}
