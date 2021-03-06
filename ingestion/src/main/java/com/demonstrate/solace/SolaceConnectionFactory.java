package com.demonstrate.solace;

import com.demonstrate.solace.connection.SolaceJcsmpConnectionFactoryStrategyTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SolaceConnectionFactory extends SolaceJcsmpConnectionFactoryStrategyTemplate {

    @Value("${solace.host}")
    private String solaceHost;

    @Value("${solace.vpn")
    private String solaceVpn;

    @Value("${solace.username}")
    private String solaceUsername;

    @Value("${solace.password}")
    private String solacePassword;

    public SolaceConnectionFactory(String solaceHost, String solaceVpn, String solaceUsername, String solacePassword) {
        super(solaceHost, solaceVpn, solaceUsername, solacePassword);
    }

    @Override
    protected void initializeAuthenticationScheme() {
        //TO DO
    }

    @Override
    protected void initializeAcknowledgementScheme() {
        //TO DO
    }

    @Override
    protected void initializeSslScheme() {
        //TO DO
    }
}
