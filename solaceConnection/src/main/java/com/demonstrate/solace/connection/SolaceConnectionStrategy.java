package com.demonstrate.solace.connection;

import com.demonstrate.error.SolaceConnectionException;
import com.google.common.base.Preconditions;

@FunctionalInterface
public interface SolaceConnectionStrategy<T> {

    T getSolaceSession() throws SolaceConnectionException;

    default void validateDefaultParameters(String solaceHost, String solaceVpn, String solaceUsername, String solacePassword) {
        Preconditions.checkNotNull(solaceHost, "Solace Host cannot be null");
        Preconditions.checkNotNull(solaceVpn, "Solace VPN cannot be null");
        Preconditions.checkNotNull(solaceUsername, "Solace Username cannot be null");
        Preconditions.checkNotNull(solacePassword, "Solace Password cannot be null");
    }
}
