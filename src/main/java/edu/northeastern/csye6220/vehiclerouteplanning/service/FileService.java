package edu.northeastern.csye6220.vehiclerouteplanning.service;

public interface FileService {

	boolean isAuthenticationByPassFilePresent();
	
	boolean isFilePresent(String filePath);
	
}
