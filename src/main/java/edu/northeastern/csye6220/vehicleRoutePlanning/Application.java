package edu.northeastern.csye6220.vehicleRoutePlanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"edu.northeastern.csye6220"})
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		LOGGER.info("Starting the Vehicle Route Planning Application");
		SpringApplication.run(Application.class, args);
	}

}
