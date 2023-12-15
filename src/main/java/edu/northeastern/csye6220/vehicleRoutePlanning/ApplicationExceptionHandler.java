package edu.northeastern.csye6220.vehicleRoutePlanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.Message;

@ControllerAdvice
public class ApplicationExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

	@ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Message> handleRuntimeException(RuntimeException exception) {
		LOGGER.error("handling exception: {}", exception.getMessage(), exception);
		Message message = new Message(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(message);
    }
	
}
