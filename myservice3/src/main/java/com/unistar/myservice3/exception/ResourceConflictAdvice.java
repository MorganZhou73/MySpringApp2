package com.unistar.myservice3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ResourceConflictAdvice {
	@ResponseBody
	@ExceptionHandler(ResourceConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String employeeExistedHandler(ResourceConflictException ex) {
		return ex.getMessage();
	}
}
