package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/update")
public class LikeController extends AbstractController {

	private static final String ADMIN = "isAdmin";
	private static final String MEMBER = "isMember";

	@PostMapping(value = "/likes")
	public @ResponseBody Map<String, Object> updateLikes(@AuthenticationPrincipal final User user, @RequestBody final Map<String, String> data) {
		Map<String, Object> response = new HashMap<>();

		final Long validPostID = Long.valueOf(data.get("postID"));
		final boolean validLike = Boolean.parseBoolean(data.get("like"));
		final String userRole = getGroupFacade().determineUserRoleInGroup(user.getUsername(), getPostFacade().findPost(validPostID).getPostGroup());

		if (userRole.equals(ADMIN) || userRole.equals(MEMBER)) {
			if (validLike) {
				getPostFacade().updateLikes(user.getUsername(), validPostID);
			} else {
				getPostFacade().updateDislikes(user.getUsername(), validPostID);
			}
			response.put("likes", getPostFacade().findPost(validPostID).getLikes());
			response.put("dislikes", getPostFacade().findPost(validPostID).getDislikes());
		}

		return response;
	}
}
