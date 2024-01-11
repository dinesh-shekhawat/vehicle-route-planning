package edu.northeastern.csye6220.vehiclerouteplanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"edu.northeastern.csye6220.vehiclerouteplanning", 
		"edu.northeastern.csye6220.vehiclerouteplanning.service", 
		"edu.northeastern.csye6220.vehiclerouteplanning.controller",
		"edu.northeastern.csye6220.vehiclerouteplanning.properties",
		"edu.northeastern.csye6220.vehiclerouteplanning.filter",
		"edu.northeastern.csye6220.vehiclerouteplanning.repository",
		"edu.northeastern.csye6220.vehiclerouteplanning.hibernate"
})
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		LOGGER.info("Starting the Vehicle Route Planning Application");
		SpringApplication.run(Application.class, args);
	}

}
