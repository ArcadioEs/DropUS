package org.engineer.work.dto;

import java.util.Calendar;
import java.util.List;

/**
 * Post data transfer object.
 */
public class PostDTO {

    private Long id;
    private String author;
    private String group;
    private String postContent;
    private Calendar date;

    private List<String> likes;
    private List<String> dislikes;

    public PostDTO() {
        this.setDate(Calendar.getInstance());
    }

    public Long getId() {
        return id;
    }

    public PostDTO setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public PostDTO setAuthor(final String author) {
        this.author = author;
        return this;
    }

    public String getPostGroup() {
        return group;
    }

    public PostDTO setPostGroup(final String group) {
        this.group = group;
        return this;
    }

    public String getPostContent() {
        return postContent;
    }

    public PostDTO setPostContent(final String postContent) {
        this.postContent = postContent;
        return this;
    }

    public Calendar getDate() {
        return date;
    }

    public PostDTO setDate(final Calendar date) {
        this.date = date;
        return this;
    }

    public List<String> getLikes() {
        return likes;
    }

    public PostDTO setLikes(final List<String> likes) {
        this.likes = likes;
        return this;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public PostDTO setDislikes(final List<String> dislikes) {
        this.dislikes = dislikes;
        return this;
    }
}
