package com.demonstrate.ingestion;

import com.demonstrate.error.SolaceConnectionException;
import com.demonstrate.solace.SolaceUtil;
import com.solacesystems.jcsmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(MessageListener.class);
    private final String destination;
    private final boolean isTransacted;
    private final boolean isXaTransacted;
    private final boolean isCacheTransacted;

    @Autowired
    private SolaceUtil solaceUtil;

    public MessageListener(String destination, boolean isTransacted, boolean isXaTransacted, boolean isCacheTransacted) {
        this.destination = destination;
        this.isTransacted = isTransacted;
        this.isXaTransacted = isXaTransacted;
        this.isCacheTransacted = isCacheTransacted;
    }

    public void listen() throws SolaceConnectionException {

        JCSMPSession jcsmpSession = solaceUtil.getSolaceConnectionFactory().getSolaceSession();
        solaceUtil.initializeConnection(jcsmpSession);

        XMLMessageConsumer cons = null;
        try {
            cons = jcsmpSession.getMessageConsumer(new XMLMessageListener() {

                @Override
                public void onReceive(BytesXMLMessage msg) {
                    if (msg instanceof TextMessage) {
                        LOG.trace("TextMessage received: {}",
                                ((TextMessage) msg).getText());
                    } else {
                        LOG.trace("Message received.");
                    }
                    LOG.trace("Message Dump: {}", msg.dump());
                }

                @Override
                public void onException(JCSMPException e) {
                    System.out.printf("Consumer received exception: %s%n", e);
                }
            });
            solaceUtil.addSubscription(destination, jcsmpSession);
            cons.start();
        } catch (JCSMPException e) {
            throw new SolaceConnectionException(e);
        } finally {
            if (cons != null) {
                cons.stop();
                cons.close();
            }
            if (jcsmpSession != null && !jcsmpSession.isClosed()) {
                jcsmpSession.closeSession();
            }
        }


    }

}
