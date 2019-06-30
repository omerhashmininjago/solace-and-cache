package com.demonstrate.error;

public class SolaceConnectionException extends Exception {

    public SolaceConnectionException(String message) {
        super(message);
    }

    public SolaceConnectionException(Throwable cause) {
        super(cause);
    }

    public SolaceConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
