package com.demonstrate.transformer;

import com.demonstrate.error.InvalidMessageException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public enum PayloadToObject {

    PAYLOAD_TO_OBJECT;

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T getObject(String payload, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(payload, clazz);
        } catch (IOException e) {
            throw new InvalidMessageException(e);
        }
    }
}
