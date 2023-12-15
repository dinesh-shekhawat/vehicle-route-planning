package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/otp")
public class OtpController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OtpController.class);

	@GetMapping()
	public String render() {
		LOGGER.trace("render called");
		return "otp";
	}
	
}
