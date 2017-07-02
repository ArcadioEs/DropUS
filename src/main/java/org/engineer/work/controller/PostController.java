package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.GroupDTO;
import org.engineer.work.dto.PostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

import static org.thymeleaf.util.StringUtils.capitalize;

/**
 * Controller for post page.
 */
@Controller
@RequestMapping(value = "/post")
public class PostController extends AbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(PostController.class);
	private static final String POST_CREATION_FAILURE = "postFailure";

	@Resource
	private GroupController groupController;

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String createPost(@RequestParam(value = "postContent") final String postContent,
	                         @RequestParam(value = "groupName") final String groupName,
	                         @AuthenticationPrincipal User user,
	                         final Model model) {
		final String validGroupName = capitalize(groupName.trim().toLowerCase());
		final String validPostContent = this.validatePostContent(postContent, model);

		final GroupDTO group = getGroupFacade().getGroupByName(validGroupName);
		if (user != null && group != null && validPostContent != null) {
				getPostFacade().createPost(new PostDTO().setAuthor(user.getUsername())
						.setPostGroup(group.getName())
						.setPostContent(validPostContent));
		}
		return groupController.getSpecificGroup(validGroupName, user, model);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deletePost(@RequestParam(value = "postID") final String postID,
	                         @AuthenticationPrincipal User user,
	                         final Model model) {
		try {
			final Long validPostID = Long.valueOf(postID);

			final PostDTO post = getPostFacade().findPost(validPostID);
			if (post != null && user != null) {
				final GroupDTO group = getGroupFacade().getGroupByName(post.getPostGroup());
				if (post.getAuthor().equals(user.getUsername()) || group.getGroupOwner().equals(user.getUsername())) {
					getPostFacade().deletePost(validPostID);
					return groupController.getSpecificGroup(group.getName(), user, model);
				}
			}
		} catch (NumberFormatException e) {
			LOG.warn("Given post id for deleting post operation is not valid, long value required");
		}
		return groupController.getGroupPage(user, model);
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
	private String validatePostContent(final String postContent, final Model model) {
		String result = null;
		if (postContent != null && postContent.length() <= 1024) {
			if (!postContent.trim().isEmpty()) {
				final String[] words = postContent.split("\\s");
				boolean invalid = true;
				for (String word : words) {
					if (word.length() > 60) {
						invalid = false;
						model.addAttribute(POST_CREATION_FAILURE, "Single words cannot be bigger than 60 digits each");
						break;
					}
				}
				if (invalid) result = postContent.trim();
			} else {
				model.addAttribute(POST_CREATION_FAILURE, "Post content cannot be empty");
			}
		} else {
			model.addAttribute(POST_CREATION_FAILURE, "Post is too large");
		}
		return result;
	}
}
