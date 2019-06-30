package com.demonstrate.ingestion;

import com.demonstrate.error.SolaceConnectionException;
import com.demonstrate.solace.SolaceUtil;
import com.solacesystems.jcsmp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private final String destination;
    private final boolean isTransacted;
    private final boolean isXaTransacted;
    private final boolean isCacheTransacted;

    public MessageListener(String destination, boolean isTransacted, boolean isXaTransacted, boolean isCacheTransacted) {
        this.destination = destination;
        this.isTransacted = isTransacted;
        this.isXaTransacted = isXaTransacted;
        this.isCacheTransacted = isCacheTransacted;
    }

    @Autowired
    private SolaceUtil solaceUtill;

    public void listen() throws SolaceConnectionException {

       solaceUtill.initializeConnection();

    }

}
