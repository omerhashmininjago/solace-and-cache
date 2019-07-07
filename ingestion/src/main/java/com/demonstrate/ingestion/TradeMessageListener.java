package com.demonstrate.ingestion;

import com.demonstrate.domain.Trade;
import com.demonstrate.error.AppException;
import com.demonstrate.error.SolaceConnectionException;
import com.demonstrate.listener.AbstractMessageListener;
import com.demonstrate.service.TradeService;
import com.demonstrate.solace.util.SolaceJscmpUtil;
import com.demonstrate.transformer.PayloadToObject;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.demonstrate.transformer.MessageToPayloadTransformer.MESSAGE_TO_PAYLOAD_TRANSFORMER;

@Component
public class TradeMessageListener extends AbstractMessageListener implements Callable {

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

    private final SolaceJscmpUtil solaceJscmpUtil;

    private final TradeService tradeService;

    @Autowired
    public TradeMessageListener(String destination, boolean isTransacted, boolean isXaTransacted, boolean isCacheTransacted, SolaceJscmpUtil solaceJscmpUtil, TradeService tradeService) {
        super(destination, isTransacted, isXaTransacted, isCacheTransacted);
        this.solaceJscmpUtil = solaceJscmpUtil;
        this.tradeService = tradeService;
    }

    /**
     * Based on the concurrency Level, we can initiate N listeners listening to the queue
     */
    public void onExecute() {

        ExecutorService executorService = Executors.newFixedThreadPool(concurrentConsumers);
        int threadsActive = concurrentConsumers;
        List<Future> futures = new ArrayList<>();
        while (threadsActive > 0) {
            futures.add(executorService.submit(this));
            threadsActive--;

            for (Future future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    // in case a listener goes down, another listener will be triggered
                    threadsActive++;
                }
            }
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
    public void consumeMessage(BytesXMLMessage msg) throws AppException {
        String payload = MESSAGE_TO_PAYLOAD_TRANSFORMER.getPayload(msg);
        Trade trade = PayloadToObject.getObject(payload, Trade.class);
        tradeService.processTrade(trade);
    }

    @Override
    public Object call() throws Exception {
        listen();
        return null;
    }
}
