package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.exception.StorageException;
import org.engineer.work.exception.StorageFileNotFoundException;
import org.engineer.work.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.DISPLAY_USER_PROFILE;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.REDIRECTION_PREFIX;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_USER_PROFILE;

/**
 * Controller to manage user's profile.
 */
@Controller
@RequestMapping("/profile")
public class UserProfilController extends AbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(UserProfilController.class);
	private static final String SHARED = "/shared/";
	private static final String NOT_SHARED = "/not_shared/";

	// TEMP
	@javax.annotation.Resource
	private StorageService storageService;

	@GetMapping("/display/{username}")
	public String getProfile(@PathVariable(value = "username") final String username, final Model model) throws IOException {
		final String validUsername = StringUtils.capitalize(username.toLowerCase());
		final UserDTO user = getUserFacade().getUserByUsername(validUsername);
		if (user != null) {
			model.addAttribute("userExists", true);
			model.addAttribute("userDetails", user);
			model.addAttribute("sharedFiles", getStorageFacade().getUserSharedFiles(validUsername));
			model.addAttribute("privateFiles", getStorageFacade().getUserPrivateFiles(validUsername));
		} else {
			model.addAttribute("userExists", false);
		}
		return TEMPLATE_USER_PROFILE;
	}

	@PostMapping("/savefiles")
	public String handleFileUpload(final MultipartFile[] files,
	                               @AuthenticationPrincipal User user) throws IOException {
		try {
			for (final MultipartFile file : files) {
				storageService.store(file, user.getUsername());
			}
		} catch (StorageException e) { //NOSONAR
			LOG.warn("Cannot store empty file.");
		}
		return REDIRECTION_PREFIX + DISPLAY_USER_PROFILE + user.getUsername();
	}

	@GetMapping("/getfile/{username}/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable final String filename,
	                                          @PathVariable final String username) {
		final Resource file = storageService.loadAsResource(username + SHARED + filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/makepublic")
	public String makeFilePublic(@RequestParam("filename") final String filename,
	                             @AuthenticationPrincipal User user) {
		final String sourceFile = getStorageProperties().getLocation() + user.getUsername() + NOT_SHARED + filename;
		final String destination = getStorageProperties().getLocation() + user.getUsername() + SHARED + filename;

		this.moveFile(sourceFile, destination);

		return REDIRECTION_PREFIX + DISPLAY_USER_PROFILE + user.getUsername();
	}

	@PostMapping("/makeprivate")
	public String makeFilePrivate(@RequestParam("filename") final String filename,
	                              @AuthenticationPrincipal User user) {
		final String sourceFile = getStorageProperties().getLocation() + user.getUsername() + SHARED + filename;
		final String destination = getStorageProperties().getLocation() + user.getUsername() + NOT_SHARED + filename;

		this.moveFile(sourceFile, destination);

		return REDIRECTION_PREFIX + DISPLAY_USER_PROFILE + user.getUsername();
	}

	@PostMapping("deletefile")
	public String deleteFile(@RequestParam("filename") final String filename,
	                         @AuthenticationPrincipal User user) {
		final String filePath = getStorageProperties().getLocation() + user.getUsername() + NOT_SHARED + filename;
		final File file = new File(filePath);
		if (file.exists() && file.canWrite()) {
			if (!file.delete()) {
				LOG.warn("Could not delete file {}", filename);
			}
		}
		return REDIRECTION_PREFIX + DISPLAY_USER_PROFILE + user.getUsername();
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(IllegalStateException.class)
	public String foo() {
		return "error";
	}

	private void moveFile(final String src, final String dest) {
		final File file = new File(src);
		if (file.exists() || file.canWrite()) {
			if (!file.renameTo(new File(dest))) {
				LOG.warn("Could not move file {}", file.getName());
			}
		}
	}
}
