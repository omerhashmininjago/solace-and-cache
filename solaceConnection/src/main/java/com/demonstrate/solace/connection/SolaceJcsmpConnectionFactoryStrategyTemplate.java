package com.demonstrate.solace.connection;

import com.demonstrate.error.SolaceConnectionException;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;

public abstract class SolaceJcsmpConnectionFactoryStrategyTemplate implements SolaceConnectionStrategy<JCSMPSession> {

    private final String solaceHost;
    private final String solaceVpn;
    private final String solaceUsername;
    private final String solacePassword;
    protected JCSMPProperties properties;

    protected SolaceJcsmpConnectionFactoryStrategyTemplate(String solaceHost, String solaceVpn, String solaceUsername, String solacePassword) {
        validateDefaultParameters(solaceHost, solaceVpn, solaceUsername, solacePassword);
        this.solaceHost = solaceHost;
        this.solaceVpn = solaceVpn;
        this.solaceUsername = solaceUsername;
        this.solacePassword = solacePassword;
        properties = new JCSMPProperties();
    }

    public JCSMPSession getSolaceSession() throws SolaceConnectionException {
        initializeDefaultSolaceProperties();
        initializeAuthenticationScheme();
        initializeAcknowledgementScheme();
        initializeSslScheme();
        try {
            return JCSMPFactory.onlyInstance().createSession(properties);
        } catch (InvalidPropertiesException e) {
            throw new SolaceConnectionException("InvalidPropertiesException");
        }
    }

    private void initializeDefaultSolaceProperties() {
        properties.setProperty(JCSMPProperties.HOST, solaceHost);     // host:port
        properties.setProperty(JCSMPProperties.USERNAME, solaceUsername); // client-username
        properties.setProperty(JCSMPProperties.VPN_NAME, solaceVpn); // message-vpn
        properties.setProperty(JCSMPProperties.PASSWORD, solacePassword); // client-password
    }

    protected abstract void initializeAuthenticationScheme();

    protected abstract void initializeAcknowledgementScheme();

    protected abstract void initializeSslScheme();
}
