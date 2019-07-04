package com.demonstrate.error;

public class InvalidMessageException extends RuntimeException {

    public InvalidMessageException(String message) {
        super(message);
    }

    public InvalidMessageException(Throwable cause) {
        super(cause);
    }
}
