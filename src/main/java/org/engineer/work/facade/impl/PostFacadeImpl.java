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
        return postService.getPostsForSpecificGroup(groupName).stream().map(post -> this.convertEntityToDTO(post)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean createPost(final PostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @Override
    @Transactional
    public boolean deletePost(final Long id) {
        return postService.deletePost(id);
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
                    .setDate(postEntity.getDate());
        } else {
            LOG.warn("Post entity is null, therefore cannot be converted to DTO");
        }
        return postDTO;
    }
}
