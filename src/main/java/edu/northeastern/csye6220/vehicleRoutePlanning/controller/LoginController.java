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
@RequestMapping("/login")
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	private final UserService userService;
	private final EmailService emailService;
	private final OtpService otpService;
	
	private final HttpSession httpSession;
	
	@Autowired
	public LoginController(
			UserService userService, 
			EmailService emailService,
			OtpService otpService,
			HttpSession httpSession) {
		this.userService = userService;
		this.emailService = emailService;
		this.otpService = otpService;
		
		this.httpSession = httpSession;
	}
	
	@GetMapping
	public String get() {
		LOGGER.trace("accessing get method");
		
		Object object = httpSession.getAttribute(Constants.JWT_TOKEN);
		if (object != null && object instanceof String) {
			LOGGER.trace("redirect to routing page as token is present");
			return "redirect:/vehicle-routing";
		}
		
		return "login";
	}
	
	@PostMapping("/add")
	public ModelAndView add(@RequestParam(name = Constants.FIELD_EMAIL) String email) {
		LOGGER.trace("add login email: {}", email);
		ModelAndView modelAndView = new ModelAndView();
		
		Object object = httpSession.getAttribute(Constants.JWT_TOKEN);
		if (object != null && object instanceof String) {
			LOGGER.trace("redirect to routing page as token is present");
			modelAndView.setViewName("redirect:/vehicle-routing");
			return modelAndView;
		}
		
		boolean errorsPresent = false;
		
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
			if (user == null) {
				errorsPresent = true;
				modelAndView.addObject("noUser", "true");
			}
		}
		
		if (errorsPresent) {
			modelAndView.addObject(Constants.FIELD_EMAIL, email);
			LOGGER.trace("there are errors in the form");
			modelAndView.setViewName("redirect:/login");
		} else {
			LOGGER.debug("Form looks valid will create a new user in the system");
			
			User user = userService.findByEmailIdAndNotDeleted(email);
			Otp otp = otpService.generateNew(user);
			LOGGER.info("system created otp: {}, for already logged in user: {}", otp, user);
			
			emailService.sendOtpMessage(otp, user);
			
			modelAndView.addObject("email", user.getEmail());
			modelAndView.setViewName("redirect:/otp");
		}
		
		return modelAndView;
	}
	
}
