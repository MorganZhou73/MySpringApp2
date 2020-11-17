package com.unistar.myservice2.exception;

import com.unistar.myservice2.exception.EmployeeExistedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmployeeExistedAdvice {
	@ResponseBody
	@ExceptionHandler(EmployeeExistedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String employeeExistedHandler(EmployeeExistedException ex) {
		return ex.getMessage();
	}
}
