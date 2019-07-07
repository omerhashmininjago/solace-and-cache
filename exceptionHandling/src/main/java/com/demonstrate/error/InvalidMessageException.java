package com.demonstrate.error;

public class InvalidMessageException extends RuntimeException {

    public InvalidMessageException(final String message) {
        super(message);
    }

    public InvalidMessageException(final Throwable cause) {
        super(cause);
    }
}
