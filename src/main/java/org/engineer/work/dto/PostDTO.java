package org.engineer.work.dto;

import java.util.Calendar;

/**
 * Post data transfer object.
 */
public class PostDTO {

    private String group;
    private String postContent;
    private Calendar date;

    public PostDTO() {
        this.date = Calendar.getInstance();
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
}
