package com.demonstrate.ingestion;

import com.demonstrate.error.SolaceConnectionException;
import com.demonstrate.listener.JscmpMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageInboundAdaptor {

    @Autowired
    JscmpMessageListener messageListener;

    public void readQueue() throws SolaceConnectionException {
        messageListener.onExecute();
    }
}
