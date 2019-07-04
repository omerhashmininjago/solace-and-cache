package com.demonstrate.ingestion;

import com.demonstrate.domain.Trade;
import com.demonstrate.error.SolaceConnectionException;
import com.demonstrate.listener.AbstractMessageListener;
import com.demonstrate.solace.util.SolaceJscmpUtil;
import com.demonstrate.transformer.PayloadToObject;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.demonstrate.transformer.MessageToPayloadTransformer.MESSAGE_TO_PAYLOAD_TRANSFORMER;

@Component
public class TradeMessageListener<T> extends AbstractMessageListener implements Callable {

    @Value("")
    private int concurrentConsumers;

    @Value("")
    private String destination;

    @Value("")
    private boolean isTransacted;

    @Value("")
    private boolean isXaTransacted;

    @Value("")
    private boolean isCacheTransacted;

    @Autowired
    private SolaceJscmpUtil solaceJscmpUtil;

    public TradeMessageListener(String destination, boolean isTransacted, boolean isXaTransacted, boolean isCacheTransacted) {
        super(destination, isTransacted, isXaTransacted, isCacheTransacted);
    }

    /**
     * Based on the concurrency Level, we can initiate N listeners listening to the queue
     */
    public void onExecute() {

        ExecutorService executorService = Executors.newFixedThreadPool(concurrentConsumers);
        int threadsActive = concurrentConsumers;
        while (threadsActive > 0) {
            executorService.submit(this);
            threadsActive--;
        }
    }

    /**
     * Get Session
     *
     * @throws SolaceConnectionException
     */
    @Override
    public void getSession() throws SolaceConnectionException {
        jcsmpSession = (JCSMPSession) solaceJscmpUtil.getSolaceConnectionFactory().getSolaceSession();
    }

    /**
     * Initializing the session
     *
     * @param jcsmpSession
     * @throws SolaceConnectionException
     */
    @Override
    public void initializeSession(JCSMPSession jcsmpSession) throws SolaceConnectionException {
        solaceJscmpUtil.initializeConnection(jcsmpSession);
    }

    @Override
    public void consumeMessage(BytesXMLMessage msg) {
        // ToDo :: Load data in Cache

        String payload = MESSAGE_TO_PAYLOAD_TRANSFORMER.getPayload(msg);
        Trade trade = PayloadToObject.getObject(payload, Trade.class);
    }

    @Override
    public Object call() throws Exception {
        listen();
        return null;
    }
}
