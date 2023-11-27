package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@GetMapping("/")
	public String home() {
		LOGGER.trace("accessing get method");
		return "index";
	}
	
}
