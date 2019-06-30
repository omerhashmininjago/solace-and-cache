package com.demonstrate.solace;

import com.demonstrate.error.SolaceConnectionException;
import com.solacesystems.jcsmp.*;
import com.solacesystems.jcsmp.transaction.TransactedSession;
import com.solacesystems.jcsmp.transaction.xa.XASession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolaceUtil {

    private final static Logger LOG = LoggerFactory.getLogger(SolaceUtil.class);
    private final static int RETRY_CONNECTION = 3;

    @Autowired
    private SolaceConnectionFactory solaceConnectionFactory;

    public void initializeConnection(JCSMPSession jcsmpSession) throws SolaceConnectionException {
        int retry = RETRY_CONNECTION;
        while (true) {
            retry--;
            try {
                jcsmpSession.connect();
                break;
            } catch (JCSMPException e) {
                if (retry == 0) {
                    throw new SolaceConnectionException("Tried connecting three times...", e);
                }
                LOG.warn("Unable to establish connection, retrying...");
            }
        }
    }

    public CacheSession startCacheSession() throws SolaceConnectionException {
        CacheSessionProperties cacheSessionProperties = new CacheSessionProperties("cacheName", 10000, 10, 1000);

        try {
            return solaceConnectionFactory.getSolaceSession().createCacheSession(cacheSessionProperties);
        } catch (JCSMPException e) {
            throw new SolaceConnectionException(e);
        }
    }

    public TransactedSession startTransactedSession() throws SolaceConnectionException {
        try {
            return solaceConnectionFactory.getSolaceSession().createTransactedSession();
        } catch (JCSMPException e) {
            throw new SolaceConnectionException(e);
        }
    }

    public XASession startXASession() throws SolaceConnectionException {
        try {
            return solaceConnectionFactory.getSolaceSession().createXASession();
        } catch (JCSMPException e) {
            throw new SolaceConnectionException(e);
        }
    }

    public void addSubscription(String destination, JCSMPSession jcsmpSession) throws SolaceConnectionException {
        final Topic topic = JCSMPFactory.onlyInstance().createTopic(destination);
        try {
            jcsmpSession.addSubscription(topic);
        } catch (JCSMPException e) {
            throw new SolaceConnectionException(e);
        }
    }

    public SolaceConnectionFactory getSolaceConnectionFactory() {
        return solaceConnectionFactory;
    }

}
