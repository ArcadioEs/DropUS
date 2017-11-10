package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.exception.StorageException;
import org.engineer.work.exception.StorageFileNotFoundException;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.DISPLAY_USER_PROFILE;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.REDIRECTION_PREFIX;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_ERROR_PAGE;
import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_USER_PROFILE;

/**
 * Controller to manage user's profile.
 */
@Controller
@RequestMapping("/profile")
public class UserProfilController extends AbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(UserProfilController.class);

	@GetMapping("/display/{username}")
	public String getProfile(@PathVariable(value = "username") final String username, final Model model) {
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
	                               @AuthenticationPrincipal User user,
	                               final RedirectAttributes redirectAttributes) {
		try {
			for (final MultipartFile file : files) {
				getStorageFacade().storeFile(file, user.getUsername());
			}
		} catch (StorageException e) { //NOSONAR
			LOG.warn("Cannot store empty file.");
		} catch (MaxUploadSizeExceededException e) {
			final double maxFileSize = getStorageFacade().getActualMaximumFileSize() / 1000;
			redirectAttributes.addFlashAttribute("uploadFileError", "File you are trying to upload is too big. The limit is " + String.format("%.2f", maxFileSize) + "KB");
		}
		return REDIRECTION_PREFIX + DISPLAY_USER_PROFILE + user.getUsername();
	}

	@GetMapping("/getfile/{username}/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable final String filename,
	                                          @PathVariable final String username) {
		final Resource file = getStorageFacade().getFile(username, filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/makepublic")
	public String makeFilePublic(@RequestParam("filename") final String filename,
	                             @AuthenticationPrincipal User user) {
		getStorageFacade().makeFilePublic(user.getUsername(), filename);
		return REDIRECTION_PREFIX + DISPLAY_USER_PROFILE + user.getUsername();
	}

	@PostMapping("/makeprivate")
	public String makeFilePrivate(@RequestParam("filename") final String filename,
	                              @AuthenticationPrincipal User user) {
		getStorageFacade().makeFilePrivate(user.getUsername(), filename);
		return REDIRECTION_PREFIX + DISPLAY_USER_PROFILE + user.getUsername();
	}

	@PostMapping("/deletefile")
	public String deleteFile(@RequestParam("filename") final String filename,
	                         @AuthenticationPrincipal User user) {
		getStorageFacade().deleteFile(user.getUsername(), filename);
		return REDIRECTION_PREFIX + DISPLAY_USER_PROFILE + user.getUsername();
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(Exception.class)
	public String getErrorPage() {
		return TEMPLATE_ERROR_PAGE;
	}


}
