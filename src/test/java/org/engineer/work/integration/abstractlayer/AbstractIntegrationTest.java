package org.engineer.work.integration.abstractlayer;

import org.engineer.work.abstractlayer.AbstractTest;
import org.engineer.work.facade.GroupFacade;
import org.engineer.work.facade.UserFacade;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.PostService;
import org.engineer.work.service.PropertiesService;
import org.engineer.work.service.UserGroupsService;
import org.engineer.work.service.UserService;

import javax.annotation.Resource;

public abstract class AbstractIntegrationTest extends AbstractTest {

	@Resource
	private UserService userService;
	@Resource
	private UserFacade userFacade;
	@Resource
	private GroupService groupService;
	@Resource
	private GroupFacade groupFacade;
	@Resource
	private UserGroupsService userGroupsService;
	@Resource
	private PostService postService;
	@Resource
	private PropertiesService propertiesService;

	protected UserService getUserService() {
		return userService;
	}

	protected UserFacade getUserFacade() {
		return userFacade;
	}

	protected GroupService getGroupService() {
		return groupService;
	}

	protected GroupFacade getGroupFacade() {
		return groupFacade;
	}

	protected UserGroupsService getUserGroupsService() {
		return userGroupsService;
	}

	protected PostService getPostService() {
		return postService;
	}

	protected PropertiesService getPropertiesService() {
		return propertiesService;
	}
}
