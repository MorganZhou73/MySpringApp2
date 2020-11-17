package com.unistar.myservice3.exception;

public class ResourceConflictException extends RuntimeException {
	public ResourceConflictException(String msg) {
		super("The Resource has already existed: " + msg);
	}
}
