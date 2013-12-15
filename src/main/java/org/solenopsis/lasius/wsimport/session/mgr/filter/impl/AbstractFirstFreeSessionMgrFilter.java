package org.solenopsis.lasius.wsimport.session.mgr.filter.impl;

import java.util.Collection;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * Chooses the first free session manager from a collection.
 *
 * @author sfloess
 *
 */
public abstract class AbstractFirstFreeSessionMgrFilter extends AbstractSessionMgrFilter {
    /**
     * Default constructor.
     */
    protected AbstractFirstFreeSessionMgrFilter() {
    }

    /**
     * Subclasses should provide the functionality to find a session manager
     * from sessionMgrs if we cannot find one free.
     *
     * @param sessionMgrs is a collection of session managers to seek one for return.
     */
    protected abstract SessionMgr findSessionMgr(final Collection<SessionMgr> sessionMgrs) throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionMgr getSessionMgr(final Collection<SessionMgr> sessionMgrs) throws Exception {
        for (final SessionMgr sessionMgr : sessionMgrs) {
            if (sessionMgr.getSession().getRemainingLocks() > 0) {
                return sessionMgr;
            }
        }

        return findSessionMgr(sessionMgrs);
    }
}
