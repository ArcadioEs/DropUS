package org.engineer.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for wall page.
 */
@Controller
public class WallController {

	@RequestMapping("/wall")
	public String getWallPage() {
		return "wall";
	}
}
