package edu.northeastern.csye6220.vehicleRoutePlanning.service;

public interface FileService {

	boolean isAuthenticationByPassFilePresent();
	
	boolean isFilePresent(String filePath);
	
}
