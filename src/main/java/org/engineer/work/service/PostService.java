package org.engineer.work.service;

import org.engineer.work.dto.PostDTO;
import org.engineer.work.model.PostEntity;

import java.util.List;

/**
 * Service managing posts in the system.
 */
public interface PostService {

    PostEntity findPost(final Long id);

    List<PostEntity> getPostsForSpecificGroup(final String groupName);

    boolean createPost(final PostDTO postDTO);

    boolean deletePost(final Long id);
}
