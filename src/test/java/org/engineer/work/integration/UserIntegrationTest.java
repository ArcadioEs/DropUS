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

		final UserDTO user = getUserFacade().getUserByUsername(userDTO.getUsername());

		/**
		 * Checking basic information after creating user
		 */
		assertTrue(getUserFacade().userExists(userDTO.getUsername()));
		assertTrue(user.getUsername().equals(userDTO.getUsername()));
		assertTrue(user.getEnabled() == (byte) 0);

		/**
		 * Checking whether deleting user works
		 */
		getUserFacade().deleteUser(userDTO.getUsername());

		assertFalse(getUserFacade().userExists(userDTO.getUsername()));
	}

	@Test
	public void checkFileStorageMechanism() {
		final String fileName = "textFile.txt";
		
		getUserFacade().createUser(userDTO);

		final MultipartFile testFile = new MockMultipartFile(fileName, fileName, "", new byte[] {'a', 'b', 'c'});

		/**
		 * Storing file
		 */
		getStorageFacade().storeFile(testFile, userDTO.getUsername());
		assertTrue(getStorageFacade().getUserPrivateFiles(userDTO.getUsername()).contains(fileName));

		/**
		 * Making file public
		 */
		getStorageFacade().makeFilePublic(userDTO.getUsername(), fileName);
		assertTrue(getStorageFacade().getUserSharedFiles(userDTO.getUsername()).contains(fileName));
		assertTrue(getStorageFacade().getFile(userDTO.getUsername(), fileName).getFilename().equals(fileName));

		/**
		 * Making file private again (after storing its private by default)
		 */
		getStorageFacade().makeFilePrivate(userDTO.getUsername(), fileName);
		assertTrue(getStorageFacade().getUserPrivateFiles(userDTO.getUsername()).contains(fileName));

		/**
		 * Deleting file
		 */
		getStorageFacade().deleteFile(userDTO.getUsername(), fileName);
		assertFalse(getStorageFacade().getUserPrivateFiles(userDTO.getUsername()).contains(fileName));

		getUserFacade().deleteUser(userDTO.getUsername());
	}
}
