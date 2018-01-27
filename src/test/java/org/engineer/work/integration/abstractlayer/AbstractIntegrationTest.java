package org.engineer.work.integration.abstractlayer;

import org.engineer.work.abstractlayer.AbstractTest;
import org.engineer.work.facade.GroupFacade;
import org.engineer.work.facade.PostFacade;
import org.engineer.work.facade.StorageFacade;
import org.engineer.work.facade.UserFacade;

import javax.annotation.Resource;

public abstract class AbstractIntegrationTest extends AbstractTest {

	@Resource
	private UserFacade userFacade;
	@Resource
	private GroupFacade groupFacade;
	@Resource
	private StorageFacade storageFacade;
	@Resource
	private PostFacade postFacade;

	protected UserFacade getUserFacade() {
		return userFacade;
	}

	protected GroupFacade getGroupFacade() {
		return groupFacade;
	}

	protected StorageFacade getStorageFacade() {
		return storageFacade;
	}

	protected PostFacade getPostFacade() {
		return postFacade;
	}
}
