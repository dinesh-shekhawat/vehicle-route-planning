package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.csye6220.vehicleRoutePlanning.constants.Constants;
import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Otp;
import edu.northeastern.csye6220.vehicleRoutePlanning.entities.User;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.EmailService;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.OtpService;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.UserService;
import edu.northeastern.csye6220.vehicleRoutePlanning.util.EmailValidationUtil;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

	private final UserService userService;
	private final OtpService otpService;
	private final EmailService emailService;
	
	private final HttpSession httpSession;
	
	@Autowired
	public RegistrationController(
			UserService userService,
			OtpService otpService,
			EmailService emailService,
			HttpSession httpSession) {
		this.userService = userService;
		this.otpService = otpService;
		this.emailService = emailService;
		this.httpSession = httpSession;
	}
	
	@GetMapping()
	public String get() {
		LOGGER.trace("get called");
		
		Object object = httpSession.getAttribute(Constants.JWT_TOKEN);
		if (object != null && object instanceof String) {
			LOGGER.trace("redirect to routing page as token is present");
			return "redirect:/vehicle-routing";
		}
		
		return "register";
	}

	@PostMapping("/add")
	public ModelAndView add(
			@RequestParam(name = Constants.FIELD_FIRST_NAME) String firstName,
			@RequestParam(name = Constants.FIELD_LAST_NAME) String lastName,
			@RequestParam(name = Constants.FIELD_EMAIL) String email,
			@RequestParam(name = Constants.FIELD_NICKNAME) String nickname) {
		LOGGER.trace(
				"add firstName: {}, lastName: {}, email: {}",
				firstName, 
				lastName,
				email);
		ModelAndView modelAndView = new ModelAndView();
		
		Object object = httpSession.getAttribute(Constants.JWT_TOKEN);
		if (object != null && object instanceof String) {
			LOGGER.trace("redirect to routing page as token is present");
			modelAndView.setViewName("redirect:/vehicle-routing");
			return modelAndView;
		}
		
		boolean errorsPresent = false;
		if (!StringUtils.hasText(firstName)) {
			errorsPresent = true;
			modelAndView.addObject("firstNameEror", "true");
			LOGGER.trace("first name not present");
		}
		
		if (!StringUtils.hasText(lastName)) {
			errorsPresent = true;
			LOGGER.trace("last name not present");
			modelAndView.addObject("lastNameEror", "true");
		}
		
		boolean emailValid = true;
		if (!StringUtils.hasText(email)) {
			emailValid = false;
			errorsPresent = true;
			modelAndView.addObject("emailEror", "true");
			LOGGER.trace("email not present");
		} else if (!EmailValidationUtil.isValidEmail(email)) {
			emailValid = false;
			errorsPresent = true;
			LOGGER.trace("email format invalid");
			modelAndView.addObject("emailFormatEror", "true");
		}
		
		if (emailValid) {
			LOGGER.trace("will check in db for email: {}", email);
			User user = userService.findByEmailIdAndNotDeleted(email);
			if (user != null) {
				errorsPresent = true;
				modelAndView.addObject("duplicateEmail", "true");
			}
		}
		
		if (errorsPresent) {
			modelAndView.addObject(Constants.FIELD_FIRST_NAME, firstName);
			modelAndView.addObject(Constants.FIELD_LAST_NAME, lastName);
			modelAndView.addObject(Constants.FIELD_EMAIL, email);
			modelAndView.addObject(Constants.FIELD_NICKNAME, nickname);
			
			LOGGER.trace("there are errors in the form");
			modelAndView.setViewName("redirect:/register");
		} else {
			LOGGER.debug("Form looks valid will create a new user in the system");
			
			User user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setNickname(nickname);
			user.setEmail(email);
			
			user = userService.add(user);
			LOGGER.info("system created user: {}", user);
			
			Otp otp = otpService.generateNew(user);
			LOGGER.info("system created otp: {}", otp);
			
			emailService.sendOtpMessage(otp, user);
			
			modelAndView.addObject("email", user.getEmail());
			modelAndView.setViewName("redirect:/otp");
		}
		
		return modelAndView;
	}

}
