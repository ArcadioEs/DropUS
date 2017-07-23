package org.engineer.work.facade.impl;

import org.engineer.work.dto.PostDTO;
import org.engineer.work.facade.PostFacade;
import org.engineer.work.model.PostEntity;
import org.engineer.work.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of UserFacade.
 */
@Service
public class PostFacadeImpl implements PostFacade {

    public static final Logger LOG = LoggerFactory.getLogger(PostFacadeImpl.class);

    @Resource
    private PostService postService;

    @Override
    public PostDTO findPost(final Long id) {
        return this.convertEntityToDTO(postService.findPost(id));
    }

    @Override
    public List<PostDTO> getPostsForSpecificGroup(final String groupName) {
        final List<PostDTO> posts = postService.getPostsForSpecificGroup(groupName).stream().map(post -> this.convertEntityToDTO(post)).collect(Collectors.toList());
        Collections.sort(posts, Comparator.comparing(PostDTO::getDate));
        Collections.reverse(posts);

        return posts;
    }

    @Override
    @Transactional
    public boolean createPost(final PostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @Override
    @Transactional
    public boolean updatePostContent(final PostDTO postDTO) {
        boolean result = false;
        if (postDTO != null) {
            final PostEntity post = postService.findPost(postDTO.getId());
            if (post != null) {
                post.setPostContent(postDTO.getPostContent());
                result = postService.updatePost(post);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deletePost(final Long id) {
        return postService.deletePost(id);
    }

    @Override
    @Transactional
    public void updateLikes(final String username, final Long postId) {
        postService.updateLikes(username, postId);
    }

    @Override
    @Transactional
    public void updateDislikes(final String username, final Long postId) {
        postService.updateDislikes(username, postId);
    }

    /**
     * Converts given entity into DTO.
     *
     * @param postEntity entity received from service layer
     * @return properly prepared DTO
     */
    private PostDTO convertEntityToDTO(final PostEntity postEntity) {
        PostDTO postDTO = null;
        if (postEntity != null) {
            postDTO = new PostDTO()
                    .setId(postEntity.getId())
                    .setAuthor(postEntity.getAuthor())
                    .setPostGroup(postEntity.getPostGroup())
                    .setPostContent(postEntity.getPostContent())
                    .setDate(postEntity.getDate())
                    .setLikes(postEntity.getLikes())
                    .setDislikes(postEntity.getDislikes());
        } else {
            LOG.warn("Post entity is null, therefore cannot be converted to DTO");
        }
        return postDTO;
    }
}
