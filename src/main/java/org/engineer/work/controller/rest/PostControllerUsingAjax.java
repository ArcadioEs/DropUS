package org.engineer.work.controller.rest;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/like")
public class PostControllerUsingAjax extends AbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(PostControllerUsingAjax.class);

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody public ModelAndView getUpdatedLikes(@RequestParam(value = "postID") final String postID,
	                                                  @RequestParam(value = "like") final String like,
	                                                  @AuthenticationPrincipal User user,
	                                                  final ModelAndView model) {
		try {
			final Long validPostID = Long.valueOf(postID);
			final boolean validLike = Boolean.parseBoolean(like);

			if (user != null) {
				if (validLike) {
					getPostFacade().updateLikes(user.getUsername(), validPostID);
				} else {
					getPostFacade().updateDislikes(user.getUsername(), validPostID);
				}
				model.addObject("updatedPost", getPostFacade().findPost(validPostID));
			}
		} catch (NumberFormatException e) {
			LOG.warn("Given post id for updating post operation is not valid, long value required");
		}
		return model;
	}
}
