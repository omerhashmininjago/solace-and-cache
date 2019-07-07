package com.demonstrate.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(UncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        LOG.error("Uncaught exception occurred; thread {} :: exception {}", t.getName(), ExceptionUtils.getRootCauseMessage(e), e);
    }
}