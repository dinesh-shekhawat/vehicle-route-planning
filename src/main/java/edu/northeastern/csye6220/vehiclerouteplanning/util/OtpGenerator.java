package edu.northeastern.csye6220.vehiclerouteplanning.util;

import java.security.SecureRandom;
import java.util.Random;

import edu.northeastern.csye6220.vehiclerouteplanning.constants.Constants;

public class OtpGenerator {
	
	private static final Random random;
	
	static {
		random = new SecureRandom();
	}
	
	private OtpGenerator() {
		
	}
	
	public static String createRandomOtp(int digits) {
        if (digits <= 0) {
            throw new IllegalArgumentException(Constants.ERROR_DIGITS_SIZE);
        }

        int minValue = (int) Math.pow(10, (double) digits - 1);
        int maxValue = (int) Math.pow(10, digits) - 1;

        int otpValue = minValue + random.nextInt(maxValue - minValue + 1);
        return String.valueOf(otpValue);
    }
}
