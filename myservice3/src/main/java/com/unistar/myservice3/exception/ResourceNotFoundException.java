package com.unistar.myservice3.exception;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String msg) {
		super("Could not find resource: " + msg);
	}
}
