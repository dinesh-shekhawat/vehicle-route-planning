package edu.northeastern.csye6220.vehiclerouteplanning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/location")
public class LocationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@GetMapping
	public String get() {
		LOGGER.trace("accessing get method");
		return "location";
	}
}
