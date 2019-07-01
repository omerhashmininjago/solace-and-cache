package com.demonstrate.consumer;

import com.demonstrate.service.TradeService;
import com.solacesystems.jcsmp.BytesMessage;
import com.solacesystems.jcsmp.Message;
import com.solacesystems.jcsmp.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

import static com.demonstrate.transformer.MessageToPayloadTransformer.MESSAGE_TO_PAYLOAD_TRANSFORMER;

@Component
public class TradeConsumer {

    private BlockingQueue blockingQueue;

    @Autowired
    private TradeService tradeService;

    public TradeConsumer(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void consumeTrades() {

        while (true) {
            Message message;
            try {
                message = (Message) blockingQueue.take();
                String payload = MESSAGE_TO_PAYLOAD_TRANSFORMER.getPayload(message);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }

    }

}
