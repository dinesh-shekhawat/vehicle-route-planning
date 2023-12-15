package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

import java.util.Date;

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
import edu.northeastern.csye6220.vehicleRoutePlanning.service.JwtService;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.OtpService;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.UserService;
import edu.northeastern.csye6220.vehicleRoutePlanning.util.EmailValidationUtil;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/otp")
public class OtpController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OtpController.class);

	private final OtpService otpService;
	private final UserService userService;
	private final JwtService jwtService;
	
	private final HttpSession httpSession;
	
	@Autowired
	public OtpController(
			OtpService otpService, 
			UserService userService, 
			JwtService jwtService,
			HttpSession httpSession) {
		this.otpService = otpService;
		this.userService = userService;
		this.jwtService = jwtService;
		this.httpSession = httpSession;
	}
	
	@GetMapping()
	public String render() {
		LOGGER.trace("render called");
		return "otp";
	}
	
	@PostMapping("/add")
	public ModelAndView add(
			@RequestParam(name = Constants.FIELD_EMAIL) String email,
			@RequestParam(name = Constants.FIELD_OTP) String otp) {
		LOGGER.trace("check email: {}, otp: {}", email, otp);
		
		ModelAndView modelAndView = new ModelAndView();
		boolean errorsPresent = false;
		if (!StringUtils.hasText(otp)) {
			errorsPresent = true;
			modelAndView.addObject("otpError", "true");
			LOGGER.trace("otp not present");
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
			if (user == null) {
				errorsPresent = true;
				LOGGER.error("invalid email sent in otp page, this should never happen: {}", email);
				modelAndView.addObject("wrongEmail", "true");
			} else {
				Otp databaseOtp = otpService.findLatestByEmailIdAndField(email, otp);
				if (databaseOtp == null) {
					errorsPresent = true;
					LOGGER.trace("incorrect otp entered");
					modelAndView.addObject("incorrectOtp", "true");
				} else {
					LOGGER.debug("Form looks valid will create the JWT token");

					databaseOtp.setAccessedOn(new Date());
					otpService.update(databaseOtp);
					LOGGER.info("databaseOtp updated: {}", databaseOtp);
					
					String token = jwtService.generateToken(user);
					httpSession.setAttribute(Constants.JWT_TOKEN, token);
				}
			}
		}
		
		if (errorsPresent) {
			modelAndView.addObject(Constants.FIELD_EMAIL, email);
			modelAndView.addObject(Constants.FIELD_OTP, otp);
			LOGGER.trace("there are errors in the otp form");
			modelAndView.setViewName("redirect:/otp");
		} else {
			modelAndView.setViewName("redirect:/vehicle-routing");
		}
		
		return modelAndView;
	}
	
}
