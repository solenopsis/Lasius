package org.solenopsis.lasius.wsimport.common.session.mgr;

import java.util.logging.Logger;

/**
 *
 * Abstract base class for session managers.
 *
 * @author sfloess
 *
 */
public abstract class AbstractSessionMgr implements SessionMgr {

    /**
     * Used for logging.
     */
    private final Logger logger;

    /**
     * Return our logger.
     *
     * @return our logger.
     */
    protected Logger getLogger() {
        return logger;
    }

    /**
     * Default constructor.
     */
    protected AbstractSessionMgr() {
        logger = Logger.getLogger(getClass().getName());
    }
}
