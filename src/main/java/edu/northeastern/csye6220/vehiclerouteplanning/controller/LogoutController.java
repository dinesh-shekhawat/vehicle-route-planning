package edu.northeastern.csye6220.vehiclerouteplanning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.northeastern.csye6220.vehiclerouteplanning.constants.Constants;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/logout")
public class LogoutController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogoutController.class);

	private HttpSession httpSession;
	
	@Autowired
	public LogoutController(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
	
	@GetMapping
	public String logout() {
		LOGGER.info("performing the logout action");
		httpSession.removeAttribute(Constants.JWT_TOKEN);
		return "logout";
	}
}
