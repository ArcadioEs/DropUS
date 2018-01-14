package org.engineer.work.integration;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.integration.abstractlayer.AbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for user management feature.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest extends AbstractIntegrationTest {

	private UserDTO userDTO;

	@Before
	public void setUp() {
		userDTO = getCompleteUserDTO(MOCK_USERNAME);
	}

	@Test
	public void createAndDeleteUser() {
		getUserFacade().createUser(userDTO);

		final UserDTO user = getUserFacade().getUserByUsername(MOCK_USERNAME);

		assertTrue(getUserFacade().userExists(MOCK_USERNAME));
		assertTrue(user.getUsername().equals(MOCK_USERNAME));
		assertTrue(user.getEnabled() == (byte) 0);

		getUserFacade().deleteUser(MOCK_USERNAME);

		assertFalse(getUserFacade().userExists(MOCK_USERNAME));
	}

	@Test
	public void checkFileStorageDirectory() {
		getUserFacade().createUser(userDTO);
		final String userRootFilePath = getPropertiesService().getProperty(MOCK_FILES_LOCATION) + MOCK_USERNAME + "/";

		assertTrue(Files.exists(Paths.get(userRootFilePath)));

		final String sharedFolder = userRootFilePath + "shared";
		final String notSharedFolder = userRootFilePath + "not_shared";

		assertTrue(Files.exists(Paths.get(sharedFolder)));
		assertTrue(Files.exists(Paths.get(notSharedFolder)));

		getUserFacade().deleteUser(MOCK_USERNAME);

		assertFalse(Files.exists(Paths.get(userRootFilePath)));
	}
}
