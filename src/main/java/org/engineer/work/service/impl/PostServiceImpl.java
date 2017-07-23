package org.engineer.work.service.impl;

import org.engineer.work.dto.PostDTO;
import org.engineer.work.model.PostEntity;
import org.engineer.work.repository.PostRepository;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Default implementation of {@link PostService}.
 */
@Service
public class PostServiceImpl implements PostService {

    private static final Logger LOG = LoggerFactory.getLogger(PostServiceImpl.class);

    @Resource
    private PostRepository postRepository;
    @Resource
    private GroupService groupService;

    @Override
    public PostEntity findPost(final Long id) {
        PostEntity postEntity = null;
        if (id != null) {
            postEntity = postRepository.findOne(id);
        }
        return postEntity;
    }

    @Override
    public List<PostEntity> getPostsForSpecificGroup(final String groupName) {
        return groupService.getGroupByName(groupName).getPosts();
    }

    @Override
    @Transactional
    public boolean createPost(final PostDTO postDTO) {
        boolean result = false;
        if (postDTO != null && groupService.getGroupByName(postDTO.getPostGroup()) != null) {
            try {
                final PostEntity post = new PostEntity(postDTO);
                postRepository.save(post);
                result = groupService.updatePosts(post.getPostGroup(), post.getId(), TRUE);
            } catch (IllegalArgumentException e) {
                LOG.warn("Could not create post with given id", e);
            }
        } else {
            LOG.warn("Creating post failed - passed DTO is null or group does not exist");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean updatePost(final PostEntity postEntity) {
        boolean result = false;
        if (postEntity != null && postRepository.exists(postEntity.getId())) {
            postRepository.save(postEntity);
            result = true;
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deletePost(final Long id) {
        boolean result = false;
        if (id != null && postRepository.exists(id)) {
            final PostEntity post = this.findPost(id);
            groupService.updatePosts(post.getPostGroup(), post.getId(), FALSE);
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

    @Override
    @Transactional
    public void updateLikes(final String username, final Long postId) {
        if (username != null && postId != null) {
            final PostEntity post = this.findPost(postId);
            if (post != null) {
                if (post.getLikes().contains(username)) {
                    post.getLikes().remove(username);
                } else {
                    if (post.getDislikes().contains(username)) {
                        post.getDislikes().remove(username);
                    }
                    post.getLikes().add(username);
                }
            } else {
                LOG.warn("Post with id {} does not exist, could not update likes", postId);
            }
        }
    }

    @Override
    @Transactional
    public void updateDislikes(final String username, final Long postId) {
        if (username != null && postId != null) {
            final PostEntity post = this.findPost(postId);
            if (post != null) {
                if (post.getDislikes().contains(username)) {
                    post.getDislikes().remove(username);
                } else {
                    if (post.getLikes().contains(username)) {
                        post.getLikes().remove(username);
                    }
                    post.getDislikes().add(username);
                }
            } else {
                LOG.warn("Post with id {} does not exist, could not update dislikes", postId);
            }
        }
    }
}
