package org.solenopsis.lasius.wsimport.port.caller.impl;

import java.util.logging.Logger;
import org.solenopsis.lasius.wsimport.port.caller.SalesforceCallerFactory;

/**
 *
 * Abstract base class of a SalesforceCallerFactory.
 *
 * @author sfloess
 *
 */
public abstract class AbstractSalesforceCallerFactory<V> implements SalesforceCallerFactory<V> {
    /**
     * Our logger.
     */
    private final Logger logger;

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected final Logger getLogger() {
        return logger;
    }

    /**
     * Default constructor.
     */
    public AbstractSalesforceCallerFactory() {
        this.logger = Logger.getLogger(getClass().getName());
    }
}
