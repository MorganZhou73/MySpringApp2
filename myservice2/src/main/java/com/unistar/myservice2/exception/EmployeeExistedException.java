package com.unistar.myservice2.exception;

public class EmployeeExistedException extends RuntimeException {
	public EmployeeExistedException(int employeeID) {
		super("The employee ID has already existed: " + employeeID);
	}

	public EmployeeExistedException(String firstName, String lastName) {
		super(String.format("The employee name has already existed: %s %s", firstName, lastName));
	}
}
