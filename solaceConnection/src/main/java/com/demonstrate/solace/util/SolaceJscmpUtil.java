package com.demonstrate.solace.util;

import com.demonstrate.error.SolaceConnectionException;
import com.demonstrate.solace.connection.SolaceConnectionStrategy;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolaceJscmpUtil {

    private final static Logger LOG = LoggerFactory.getLogger(SolaceJscmpUtil.class);
    private final static int RETRY_CONNECTION = 3;

    @Autowired
    private SolaceConnectionStrategy solaceConnectionFactory;

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

  /*  public CacheSession startCacheSession() throws SolaceConnectionException {
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
    }*/

    public SolaceConnectionStrategy getSolaceConnectionFactory() {
        return solaceConnectionFactory;
    }
}
