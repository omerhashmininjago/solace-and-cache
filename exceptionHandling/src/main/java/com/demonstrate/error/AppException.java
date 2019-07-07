package com.demonstrate.error;

public class AppException extends RuntimeException {

    public AppException(Throwable cause) {
        super(cause);
    }
}
