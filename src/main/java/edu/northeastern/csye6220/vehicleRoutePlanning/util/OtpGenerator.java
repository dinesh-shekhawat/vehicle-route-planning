package edu.northeastern.csye6220.vehicleRoutePlanning.util;

import java.util.Random;

import edu.northeastern.csye6220.vehicleRoutePlanning.constants.Constants;

public class OtpGenerator {
	public static String createRandomOtp(int digits) {
        if (digits <= 0) {
            throw new IllegalArgumentException(Constants.ERROR_DIGITS_SIZE);
        }

        int minValue = (int) Math.pow(10, digits - 1);
        int maxValue = (int) Math.pow(10, digits) - 1;

        Random random = new Random();
        int otpValue = minValue + random.nextInt(maxValue - minValue + 1);
        return String.valueOf(otpValue);
    }
}
