package com.unistar.myservice2.exception;

public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(int employeeID) {
		super("Could not find employee " + employeeID);
	}
}
