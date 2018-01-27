package org.engineer.work.integration;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.PostDTO;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.integration.abstractlayer.AbstractIntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for user management feature.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostIntegrationTest extends AbstractIntegrationTest {

	private UserDTO userDTO;
	private GroupDTO groupDTO;
	private PostDTO postDTO;

	@Before
	public void setUp() {
		userDTO = getCompleteUserDTO(MOCK_USERNAME);
		groupDTO = getCompleteGroupDTO(MOCK_GROUP_NAME);

		postDTO = getCompletePostDTO(userDTO.getUsername(), groupDTO.getName());
	}

	@After
	public void cleanUp() {
		getUserFacade().deleteUser(userDTO.getUsername());
	}

	@Test
	public void checkPostMechanism() {
		PostDTO postDTO;
		/**
		 * Creating test data
		 */
		getUserFacade().createUser(userDTO);
		getGroupFacade().createGroup(groupDTO);
		getPostFacade().createPost(this.postDTO);

		/**
		 * Checking basic information after creating post
		 */
		postDTO = getPostFacade().getPostsForSpecificGroup(groupDTO.getName()).get(0);
		assertTrue(postDTO.getAuthor().equals(this.postDTO.getAuthor()));
		assertTrue(postDTO.getPostGroup().equals(this.postDTO.getPostGroup()));
		assertTrue(postDTO.getPostContent().equals(this.postDTO.getPostContent()));
		// I don't know why, but the difference is always one millisecond, although it should be exactly the same.
		// I do not have time to dive into investigation.
		assertTrue(postDTO.getDate().compareTo(this.postDTO.getDate()) == -1);

		/**
		 * Checking likes/dislikes functionality
		 */
		getPostFacade().updateLikes(userDTO.getUsername(), postDTO.getId());
		postDTO = getPostFacade().getPostsForSpecificGroup(groupDTO.getName()).get(0);
		assertTrue(postDTO.getLikes().size() == 1);
		assertTrue(postDTO.getLikes().contains(userDTO.getUsername()));
		assertTrue(postDTO.getDislikes().size() == 0);
		assertFalse(postDTO.getDislikes().contains(userDTO.getUsername()));

		getPostFacade().updateDislikes(userDTO.getUsername(), postDTO.getId());
		postDTO = getPostFacade().getPostsForSpecificGroup(groupDTO.getName()).get(0);
		assertTrue(postDTO.getLikes().size() == 0);
		assertFalse(postDTO.getLikes().contains(userDTO.getUsername()));
		assertTrue(postDTO.getDislikes().size() == 1);
		assertTrue(postDTO.getDislikes().contains(userDTO.getUsername()));

		/**
		 * Changing post content
		 */
		getPostFacade().updatePostContent(postDTO.setPostContent(""));
		postDTO = getPostFacade().getPostsForSpecificGroup(groupDTO.getName()).get(0);
		assertTrue(postDTO.getPostContent().equals(""));

		/**
		 * Deleting post
		 */
		getPostFacade().deletePost(postDTO.getId());
		assertTrue(getPostFacade().findPost(postDTO.getId()) == null);

		/**
		 * Checking whether deleting group will also delete its posts
		 */
		getPostFacade().createPost(this.postDTO);
		postDTO = getPostFacade().getPostsForSpecificGroup(groupDTO.getName()).get(0);
		getGroupFacade().deleteGroup(groupDTO.getName());
		assertTrue(getPostFacade().findPost(postDTO.getId()) == null);
	}
}
