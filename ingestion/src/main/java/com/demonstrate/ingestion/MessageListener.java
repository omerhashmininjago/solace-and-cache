package com.demonstrate.ingestion;

import com.demonstrate.error.SolaceConnectionException;
import com.demonstrate.listener.JscmpMessageListener;
import com.demonstrate.solace.util.SolaceJscmpUtil;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.XMLMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class MessageListener extends JscmpMessageListener {

    @Autowired
    private SolaceJscmpUtil solaceJscmpUtil;

    public MessageListener(String destination, boolean isTransacted, boolean isXaTransacted, boolean isCacheTransacted, BlockingQueue<XMLMessage> blockingQueue) {
        super(destination, isTransacted, isXaTransacted, isCacheTransacted, blockingQueue);
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
