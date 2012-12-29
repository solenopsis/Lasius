package org.solenopsis.lasius.wsimport.session.mgr.impl;

import java.util.logging.Logger;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

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
     * @return  our logger.
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
