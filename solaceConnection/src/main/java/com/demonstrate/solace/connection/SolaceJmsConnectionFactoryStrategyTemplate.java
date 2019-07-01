package com.demonstrate.solace.connection;

import com.demonstrate.error.SolaceConnectionException;
import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;

import javax.jms.JMSException;
import javax.jms.Session;

public abstract class SolaceJmsConnectionFactoryStrategyTemplate implements SolaceConnectionStrategy<Session> {

    private final String solaceHost;
    private final String solaceVpn;
    private final String solaceUsername;
    private final String solacePassword;

    private SolConnectionFactory connectionFactory;

    protected SolaceJmsConnectionFactoryStrategyTemplate(String solaceHost, String solaceVpn, String solaceUsername, String solacePassword) {
        validateDefaultParameters(solaceHost, solaceVpn, solaceUsername, solacePassword);
        this.solaceHost = solaceHost;
        this.solaceVpn = solaceVpn;
        this.solaceUsername = solaceUsername;
        this.solacePassword = solacePassword;
    }

    @Override
    public Session getSolaceSession() throws SolaceConnectionException {
        initializeDefaultSolaceProperties();
        initializeAuthenticationScheme();
        initializeAcknowledgementScheme();
        initializeSslScheme();
        try {
            return (Session) connectionFactory.createConnection();
        } catch (JMSException e) {
            throw new SolaceConnectionException(e);
        }
    }

    private void initializeDefaultSolaceProperties() throws SolaceConnectionException {
        try {
            connectionFactory = SolJmsUtility.createConnectionFactory();
        } catch (Exception e) {
            throw new SolaceConnectionException(e);
        }
        connectionFactory.setHost(solaceHost);
        connectionFactory.setVPN(solaceVpn);
        connectionFactory.setUsername(solaceUsername);
        connectionFactory.setPassword(solacePassword);
    }

    protected abstract void initializeAuthenticationScheme();

    protected abstract void initializeAcknowledgementScheme();

    protected abstract void initializeSslScheme();

}
