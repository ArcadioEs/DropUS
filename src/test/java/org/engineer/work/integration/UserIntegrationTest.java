package org.engineer.work.integration;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.integration.abstractlayer.AbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

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
	public void checkFileStorageMechanism() {
		final String fileName = "textFile.txt";
		
		getUserFacade().createUser(userDTO);

		final MultipartFile testFile = new MockMultipartFile(fileName, fileName, "", new byte[] {'a', 'b', 'c'});

		getStorageFacade().storeFile(testFile, MOCK_USERNAME);
		assertTrue(getStorageFacade().getUserPrivateFiles(MOCK_USERNAME).contains(fileName));

		getStorageFacade().makeFilePublic(MOCK_USERNAME, fileName);
		assertTrue(getStorageFacade().getUserSharedFiles(MOCK_USERNAME).contains(fileName));
		assertTrue(getStorageFacade().getFile(MOCK_USERNAME, fileName).getFilename().equals(fileName));

		getStorageFacade().makeFilePrivate(MOCK_USERNAME, fileName);
		assertTrue(getStorageFacade().getUserPrivateFiles(MOCK_USERNAME).contains(fileName));

		getStorageFacade().deleteFile(MOCK_USERNAME, fileName);
		assertFalse(getStorageFacade().getUserPrivateFiles(MOCK_USERNAME).contains(fileName));

		getUserFacade().deleteUser(MOCK_USERNAME);
	}
}
