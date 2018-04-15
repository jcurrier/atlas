package com.amazon.atlas.exceptions;

public class InternalServiceException extends Exception {
    public InternalServiceException(String message) {
        super(message);
    }

    public InternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
