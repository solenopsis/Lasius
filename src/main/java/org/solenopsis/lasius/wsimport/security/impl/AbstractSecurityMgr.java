package org.solenopsis.lasius.wsimport.security.impl;

import java.util.logging.Logger;
import org.solenopsis.lasius.wsimport.security.SecurityMgr;

/**
 *
 * Abstract base class for security web services.
 *
 * @author sfloess
 *
 */
public abstract class AbstractSecurityMgr implements SecurityMgr {
    /**
     * Our logger.
     */
    private final Logger logger;

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected Logger getLogger() {
        return logger;
    }

    /**
     * Default constructor.
     */
    protected AbstractSecurityMgr() {
        this.logger = Logger.getLogger(getClass().getName());
    }
}
