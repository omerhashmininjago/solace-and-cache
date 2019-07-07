package com.demonstrate.error;

public class InvalidTradeException extends RuntimeException {

    public InvalidTradeException(String message) {
        super(message);
    }
}
