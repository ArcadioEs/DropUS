package org.engineer.work.model;

import org.engineer.work.dto.PostDTO;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Entity representing Post in the system.
 */
@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String postGroup;
    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(max = 1024)
    private String postContent;
    @Column(nullable = false)
    private Calendar date;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> likes = new ArrayList<>();
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> dislikes = new ArrayList<>();

    public PostEntity(final PostDTO postDTO) throws IllegalArgumentException {
        this.setAuthor(postDTO.getAuthor());
        this.setPostGroup(postDTO.getPostGroup());
        this.setPostContent(postDTO.getPostContent());
        this.setDate(postDTO.getDate());
    }

    protected PostEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        throw new UnsupportedOperationException("ID cannot be initialized manually");
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        if (author != null) {
            this.author = author;
        } else {
            throw new IllegalArgumentException("Field author must not be null");
        }
    }

    public String getPostGroup() {
        return postGroup;
    }

    public void setPostGroup(final String postGroup) {
        if (postGroup != null) {
            this.postGroup = postGroup;
        } else {
            throw new IllegalArgumentException("Field postGroup must not be null");
        }
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(final String postContent) {
        if (postContent != null) {
            this.postContent = postContent;
        } else {
            throw new IllegalArgumentException("Field postContent must not be null");
        }
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(final Calendar date) {
        if (date != null) {
            this.date = date;
        } else {
            throw new IllegalArgumentException("Field date must not be null");
        }
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(final List<String> likes) {
        this.likes = likes;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(final List<String> dislikes) {
        this.dislikes = dislikes;
    }
}
