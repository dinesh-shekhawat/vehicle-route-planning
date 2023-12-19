package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.properties.AuthProperties;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.FileService;

@Service
public class FileServiceImpl implements FileService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

	private final AuthProperties authProperties;
	
	@Autowired
	public FileServiceImpl(AuthProperties authProperties) {
		this.authProperties = authProperties;
		LOGGER.info("loaded authProperties: {}", authProperties);
	}
	
	@Override
	public boolean isAuthenticationByPassFilePresent() {
		return isFilePresent(authProperties.getAuthBypassFilePath());
	}

	@Override
	public boolean isFilePresent(String filePath) {
		boolean result = false;
		
		try {
			LOGGER.info("checking for presence of filePath: {}", filePath);
			result = Files.exists(Paths.get(filePath));	
		} catch (Exception e) {
			LOGGER.error("exception while checking file: {}", e.getMessage(), e);
		}
		
		LOGGER.info("file presence: {}", result);
		return result;
	}

}
