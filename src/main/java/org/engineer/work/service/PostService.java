package org.engineer.work.service;

import org.engineer.work.dto.PostDTO;
import org.engineer.work.model.PostEntity;

/**
 * Service managing posts in the system.
 */
public interface PostService {

    PostEntity findPost(final Long id);

    boolean createPost(final PostDTO postDTO);

    boolean deletePost(final Long id);
}
