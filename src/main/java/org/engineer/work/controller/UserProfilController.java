package org.engineer.work.controller;

import org.engineer.work.controller.abstractcontroller.AbstractController;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.exception.StorageFileNotFoundException;
import org.engineer.work.facade.StorageFacade;
import org.engineer.work.service.StorageService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

import static org.engineer.work.controller.abstractcontroller.AbstractController.Templates.TEMPLATE_USER_PROFILE;

/**
 * Controller to manage user's profile.
 */
@Controller
@RequestMapping("/profile")
public class UserProfilController extends AbstractController {

	@javax.annotation.Resource
	private StorageFacade storageFacade;

	// TEMP
	@javax.annotation.Resource
	private StorageService storageService;

	@RequestMapping(value = "/display/{username}", method = RequestMethod.GET)
	public String getProfile(@PathVariable(value = "username") final String username, final Model model) throws IOException {
		final String validUsername = StringUtils.capitalize(username.toLowerCase());
		final UserDTO user = getUserFacade().getUserByUsername(validUsername);
		if (user != null) {
			model.addAttribute("userExists", true);
			model.addAttribute("userDetails", user);
			model.addAttribute("sharedFiles", storageFacade.getUserSharedFiles(validUsername));
		} else {
			model.addAttribute("userExists", false);
		}
		return TEMPLATE_USER_PROFILE;
	}

	@PostMapping("/savefile")
	public String handleFileUpload(@RequestParam("file") final MultipartFile file,
	                               @AuthenticationPrincipal User user,
	                               final Model model) throws IOException {
		storageService.store(file, user.getUsername());
		return this.getProfile(user.getUsername(), model);
	}

	@GetMapping("/getfile/{username}/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable final String filename,
	                                          @PathVariable final String username) {
		final Resource file = storageService.loadAsResource(username + "/not_shared/" + filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
}
