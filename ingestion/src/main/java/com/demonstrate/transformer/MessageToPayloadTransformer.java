package com.demonstrate.transformer;

import com.demonstrate.error.InvalidMessageException;
import com.solacesystems.jcsmp.BytesMessage;
import com.solacesystems.jcsmp.Message;
import com.solacesystems.jcsmp.TextMessage;

public enum MessageToPayloadTransformer {

    MESSAGE_TO_PAYLOAD_TRANSFORMER;

    public static String getPayload(Message message) {
        if (message instanceof TextMessage) {
            return ((TextMessage) message).getText();
        } else if (message instanceof BytesMessage) {
            BytesMessage bMessage = (BytesMessage) message;
            byte payloadBytes[] = bMessage.getBytes();
            return new String(payloadBytes);
        } else throw new InvalidMessageException("Not a valid Message Type");
    }
}
