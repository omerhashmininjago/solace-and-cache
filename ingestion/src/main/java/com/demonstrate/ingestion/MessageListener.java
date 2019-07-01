package com.demonstrate.ingestion;

import com.demonstrate.error.SolaceConnectionException;
import com.demonstrate.listener.JscmpMessageListener;
import com.demonstrate.solace.util.SolaceJscmpUtil;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.XMLMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MessageListener extends JscmpMessageListener {

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

    private BlockingQueue<XMLMessage> blockingQueue;

    @Autowired
    private SolaceJscmpUtil solaceJscmpUtil;

    public MessageListener(String destination, boolean isTransacted, boolean isXaTransacted, boolean isCacheTransacted, BlockingQueue<XMLMessage> blockingQueue) {
        super(destination, isTransacted, isXaTransacted, isCacheTransacted, blockingQueue);
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

}
