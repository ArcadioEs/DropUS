package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.PostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.DISPLAY_ALL_GROUPS;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.DISPLAY_GROUP;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.REDIRECTION_PREFIX;
import static org.thymeleaf.util.StringUtils.capitalize;

/**
 * Controller for post page.
 */
@Controller
@RequestMapping(value = "/post")
public class PostController extends AbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(PostController.class);

	private static final String POST_CREATION_FAILURE = "postCreationFailure";
	private static final String POST_UPDATE_FAILURE = "postUpdateFailure";
	private static final String CREATE_POST = "create_post";
	private static final String UPDATE_POST = "update_post";

	@PostMapping(value = "/create")
	public String createPost(@RequestParam(value = "postContent") final String postContent,
	                         @RequestParam(value = "groupName") final String groupName,
	                         @AuthenticationPrincipal User user,
	                         final RedirectAttributes redirectAttributes) {
		final String validGroupName = capitalize(groupName.trim().toLowerCase());
		final String validPostContent = this.validatePostContent(postContent, redirectAttributes, CREATE_POST);
		final String userRole = determineUserRoleInGroup(user.getUsername(), validGroupName);

		final GroupDTO group = getGroupFacade().getGroupByName(validGroupName);
		if (group != null && validPostContent != null && checkWhetherIsMemberGroup(userRole)) {
			getPostFacade().createPost(
					new PostDTO().setAuthor(user.getUsername())
							     .setPostGroup(group.getName())
								 .setPostContent(validPostContent)
			);
		}
		return (group != null) ? REDIRECTION_PREFIX + DISPLAY_GROUP + group.getName() : REDIRECTION_PREFIX + DISPLAY_ALL_GROUPS;
	}

	@PostMapping(value = "/update")
	public String updatePostContent(@RequestParam(value = "postContent") final String postContent,
	                                @RequestParam(value = "postID") final String postID,
	                                @AuthenticationPrincipal User user,
	                                final RedirectAttributes redirectAttributes) {
		final String validPostContent = this.validatePostContent(postContent, redirectAttributes, UPDATE_POST);

		PostDTO post = null;
		try {
			final Long validPostID = Long.valueOf(postID);

			post = getPostFacade().findPost(validPostID);
			if (post != null
					&& validPostContent != null) {
				final String userRole = determineUserRoleInGroup(user.getUsername(), post.getPostGroup());

				if (checkWhetherIsMemberGroup(userRole) && post.getAuthor().equals(user.getUsername())) {
					getPostFacade().updatePostContent(
							new PostDTO().setId(post.getId())
										 .setPostContent(validPostContent)
					);
				}
			}
		} catch (NumberFormatException e) {
			LOG.warn("Given post id for updating post operation is not valid, long value required");
		}
		return (post != null) ? REDIRECTION_PREFIX + DISPLAY_GROUP + post.getPostGroup() : REDIRECTION_PREFIX + DISPLAY_ALL_GROUPS;
	}

	@PostMapping(value = "/delete")
	public String deletePost(@RequestParam(value = "postID") final String postID,
	                         @AuthenticationPrincipal User user) {
		PostDTO post = null;
		try {
			final Long validPostID = Long.valueOf(postID);

			post = getPostFacade().findPost(validPostID);
			if (post != null) {
				final String userRole = determineUserRoleInGroup(user.getUsername(), post.getPostGroup());
				final GroupDTO group = getGroupFacade().getGroupByName(post.getPostGroup());

				if (checkWhetherIsMemberGroup(userRole) && checkWhetherAbleToDeletePost(post, group, user.getUsername())) {
					getPostFacade().deletePost(validPostID);
				}
			}
		} catch (NumberFormatException e) {
			LOG.warn("Given post id for deleting post operation is not valid, long value required");
		}
		return (post != null) ? REDIRECTION_PREFIX + DISPLAY_GROUP + post.getPostGroup() : REDIRECTION_PREFIX + DISPLAY_ALL_GROUPS;
	}

	/**
	 * Checks whether user is permitted to delete post
	 */
	private boolean checkWhetherAbleToDeletePost(final PostDTO post, final GroupDTO group, final String username) {
		return (post.getAuthor().equals(username) || group.getGroupOwner().equals(username));
	}

	/**
	 * This method validates if post content is valid.<br>
	 * Criteria:
	 * <ul>
	 * <li>Post must not be empty or contain only white marks</li>
	 * <li>Content must not be larger than 1024 digits</li>
	 * <li>Words contained in content cannot be larger than 60 digits each</li>
	 * </ul>
	 *
	 * @return Post content if valid, null otherwise
	 */
	private String validatePostContent(final String postContent, final RedirectAttributes redirectAttributes, String mode) { //NOSONAR
		String result = null;
		if (postContent != null && postContent.length() <= 1024) {
			if (!postContent.trim().isEmpty()) {
				final String[] words = postContent.split("\\s");
				boolean invalid = true;
				for (String word : words) {
					if (word.length() > 60) {
						invalid = false;
						if (CREATE_POST.equals(mode)) {
							redirectAttributes.addFlashAttribute(POST_CREATION_FAILURE, "Single words cannot be bigger than 60 digits each");
						} else {
							redirectAttributes.addFlashAttribute(POST_UPDATE_FAILURE, "Single words cannot be bigger than 60 digits each");
						}
						break;
					}
				}
				if (invalid) result = postContent.trim();
			} else {
				if (CREATE_POST.equals(mode)) {
					redirectAttributes.addFlashAttribute(POST_CREATION_FAILURE, "Post content cannot be empty");
				} else {
					redirectAttributes.addFlashAttribute(POST_UPDATE_FAILURE, "Post content cannot be empty");
				}
			}
		} else {
			if (CREATE_POST.equals(mode)) {
				redirectAttributes.addFlashAttribute(POST_CREATION_FAILURE, "Post is too large");
			} else {
				redirectAttributes.addFlashAttribute(POST_UPDATE_FAILURE, "Post is too large");
			}
		}
		return result;
	}
}
