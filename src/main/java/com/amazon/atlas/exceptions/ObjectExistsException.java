package com.amazon.atlas.exceptions;

public class ObjectExistsException extends Exception{
    public ObjectExistsException(String message) {
        super(message);
    }

    public ObjectExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
