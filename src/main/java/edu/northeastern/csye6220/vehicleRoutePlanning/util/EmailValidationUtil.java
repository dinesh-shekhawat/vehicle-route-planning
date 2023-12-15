package edu.northeastern.csye6220.vehicleRoutePlanning.util;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailValidationUtil {

	public static boolean isValidEmail(String email) {
		EmailValidator emailValidator = EmailValidator.getInstance();
		return emailValidator.isValid(email);
	}

}
