package com.demonstrate.listener;

import com.demonstrate.error.SolaceConnectionException;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.XMLMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.solacesystems.jcsmp.EndpointProperties.ACCESSTYPE_EXCLUSIVE;
import static com.solacesystems.jcsmp.EndpointProperties.PERMISSION_CONSUME;
import static com.solacesystems.jcsmp.JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT;
import static com.solacesystems.jcsmp.JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS;

public abstract class AbstractMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMessageListener.class);
    private final String destination;
    private final boolean isTransacted;
    private final boolean isXaTransacted;
    private final boolean isCacheTransacted;
    private EndpointProperties endpointProperties;
    private ConsumerFlowProperties flowProperties;
    private EndpointProperties endpointProps;
    protected JCSMPSession jcsmpSession;

    public AbstractMessageListener(String destination, boolean isTransacted, boolean isXaTransacted, boolean isCacheTransacted) {
        this.destination = destination;
        this.isTransacted = isTransacted;
        this.isXaTransacted = isXaTransacted;
        this.isCacheTransacted = isCacheTransacted;
    }

    public void listen() throws SolaceConnectionException {
        getSession();
        final JCSMPSession session = jcsmpSession;
        initializeSession(session);
        provisionQueue(destination, session);
        createBind(destination);
        getEndpointProperties();
        monitorQueue(session, flowProperties, endpointProperties);
        session.closeSession();
    }

    /**
     * Get Session
     *
     * @throws SolaceConnectionException
     */
    public abstract void getSession() throws SolaceConnectionException;

    /**
     * Initializing the session
     *
     * @param jcsmpSession
     * @throws SolaceConnectionException
     */
    public abstract void initializeSession(JCSMPSession jcsmpSession) throws SolaceConnectionException;

    /**
     * Consume the messages from the queue
     *
     * @param jcsmpSession
     * @param flowProperties
     * @param endpointProperties
     * @throws SolaceConnectionException
     */
    public void monitorQueue(JCSMPSession jcsmpSession, ConsumerFlowProperties flowProperties, EndpointProperties endpointProperties) throws SolaceConnectionException {
        FlowReceiver cons = null;
        while (true) {
            try {
                cons = jcsmpSession.createFlow(new XMLMessageListener() {
                    @Override
                    public void onReceive(BytesXMLMessage msg) {
                        consumeMessage(msg);
                        LOG.trace("Message Dump:%n%s%n", msg.dump());
                        // When the ack mode is set to SUPPORTED_MESSAGE_ACK_CLIENT,
                        // guaranteed delivery messages are acknowledged after
                        // processing
                        msg.ackMessage();
                    }

                    @Override
                    public void onException(JCSMPException e) {
                        LOG.error("Consumer received exception: ", e);
                    }
                }, flowProperties, endpointProperties);

                cons.start();
            } catch (JCSMPException e) {
                // ToDo
            } finally {
                if (cons != null) {
                    cons.stop();
                    cons.close();
                }
            }
        }
    }

    /**
     * Provision the queue, and do not fail if it already exists
     *
     * @param destination
     * @param jcsmpSession
     * @throws SolaceConnectionException
     */
    public void provisionQueue(String destination, JCSMPSession jcsmpSession) throws SolaceConnectionException {
        final Queue queue = getQueue(destination);
        try {
            endpointProperties = getEndpointProperties();
            jcsmpSession.provision(queue, endpointProperties, FLAG_IGNORE_ALREADY_EXISTS);
        } catch (JCSMPException e) {
            throw new SolaceConnectionException(e);
        }
    }

    /**
     * Create a Flow be able to bind to and consumer messages from the Queue.
     *
     * @param destination
     */
    public ConsumerFlowProperties createBind(String destination) {
        final Queue queue = getQueue(destination);
        flowProperties = new ConsumerFlowProperties();
        flowProperties.setEndpoint(queue);
        flowProperties.setAckMode(SUPPORTED_MESSAGE_ACK_CLIENT);
        return flowProperties;
    }

    /**
     * create the queue object locally
     *
     * @param destination
     * @return Queue
     */
    private Queue getQueue(String destination) {
        return JCSMPFactory.onlyInstance().createQueue(destination);
    }

    /**
     * set queue permissions to "consumer" and access-type to "exclusive"
     *
     * @return endpointProps
     */
    public EndpointProperties getEndpointProperties() {
        endpointProps = new EndpointProperties();
        endpointProps.setPermission(PERMISSION_CONSUME);
        endpointProps.setAccessType(ACCESSTYPE_EXCLUSIVE);
        return endpointProps;
    }

    /**
     *
     */
    public abstract void onExecute();

    /**
     * Give an implementation of how the message is to be consumed
     */
    public abstract void consumeMessage(BytesXMLMessage xmlMessage);

}
