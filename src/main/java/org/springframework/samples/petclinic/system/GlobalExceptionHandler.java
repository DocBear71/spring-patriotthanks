package org.springframework.samples.petclinic.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Catch our specific 404 exception
	// @ExceptionHandler(NoResourceFoundException.class)
	// public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
	// return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	// }

	@ExceptionHandler(NoResourceFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNotFound(NoResourceFoundException ex) {
		return "error/404";
	}

}
