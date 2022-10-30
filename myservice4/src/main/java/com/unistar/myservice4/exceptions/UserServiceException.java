package com.unistar.myservice4.exceptions;

public class UserServiceException extends RuntimeException {
    private static final long serialVersionUID = 6101435926477571452L;

    public UserServiceException(String message) {
        super(message);
    }
}
