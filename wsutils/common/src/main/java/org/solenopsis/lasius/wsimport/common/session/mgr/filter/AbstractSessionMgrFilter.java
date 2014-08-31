package org.solenopsis.lasius.wsimport.common.session.mgr.filter;

import java.util.Collection;
import java.util.logging.Logger;
import org.solenopsis.lasius.wsimport.common.session.mgr.SessionMgr;

/**
 *
 * Abstract base class for session manager filters.
 *
 * @author sfloess
 *
 */
public abstract class AbstractSessionMgrFilter implements SessionMgrFilter {

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
    protected AbstractSessionMgrFilter() {
        logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Return the session manager from sessionMgrs.
     *
     * @param index       is the index of the session manager to return.
     * @param sessionMgrs is the collection of session managers that we want the
     *                    index-th one.
     */
    protected SessionMgr getSessionMgr(final int index, final Collection<SessionMgr> sessionMgrs) {
        return (SessionMgr) sessionMgrs.toArray()[index];
    }
}
