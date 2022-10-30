package com.unistar.myservice4.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 3848353525068732973L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final Throwable cause) {
        super(cause);
    }
}
