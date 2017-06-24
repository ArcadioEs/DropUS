package org.engineer.work.service.impl;

import org.engineer.work.dto.PostDTO;
import org.engineer.work.model.PostEntity;
import org.engineer.work.repository.PostRepository;
import org.engineer.work.service.GroupPostsService;
import org.engineer.work.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static java.lang.Boolean.*;

/**
 * Default implementation of {@link PostService}.
 */
@Service
public class PostServiceImpl implements PostService {

    private static final Logger LOG = LoggerFactory.getLogger(PostServiceImpl.class);

    @Resource
    private PostRepository postRepository;
    @Resource
    private GroupPostsService groupPostsService;

    @Override
    public PostEntity findPost(final Long id) {
        PostEntity postEntity = null;
        if (id != null) {
            postEntity = postRepository.findOne(id);
        }
        return postEntity;
    }

    @Override
    public boolean createPost(final PostDTO postDTO) {
        boolean result = false;
        if (postDTO != null) {
            try {
                PostEntity post = new PostEntity(postDTO);
                postRepository.save(post);
                result = groupPostsService.createOrUpdateGroupPosts(postDTO.getPostGroup(), post.getId(), TRUE);
            } catch (IllegalArgumentException e) {
                LOG.warn("Could not create post with given id", e);
            }
        } else {
            LOG.warn("Creating post failed - passed DTO is null");
        }
        return result;
    }

    @Override
    public boolean deletePost(final Long id) {
        boolean result = false;
        if (id != null && postRepository.exists(id)) {
            postRepository.delete(id);
            result = true;
        } else {
            if (id == null) {
                LOG.warn("Could not delete group without id");
            } else {
                LOG.warn("Could not found group with id: {}", id);
            }
        }
        return result;
    }
}
