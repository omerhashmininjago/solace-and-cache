package com.demonstrate.error;

public class SolaceConnectionException extends Exception {

    public SolaceConnectionException(final String message) {
        super(message);
    }

    public SolaceConnectionException(final Throwable cause) {
        super(cause);
    }

    public SolaceConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
