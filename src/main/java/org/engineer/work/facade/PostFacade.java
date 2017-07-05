package org.engineer.work.facade;

import org.engineer.work.dto.PostDTO;

import java.util.List;

/**
 * Facade for post management.
 */
public interface PostFacade {

    PostDTO findPost(final Long id);

    List<PostDTO> getPostsForSpecificGroup(final String groupName);

    boolean createPost(final PostDTO postDTO);

    boolean updatePostContent(final PostDTO postDTO);

    boolean deletePost(final Long id);
}
