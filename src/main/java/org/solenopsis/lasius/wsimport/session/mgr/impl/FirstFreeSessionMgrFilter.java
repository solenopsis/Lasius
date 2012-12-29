package org.solenopsis.lasius.wsimport.session.mgr.impl;

import java.util.Collection;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgrFilter;

/**
 *
 * Chooses the first session manager should one not be found free.
 *
 * @author sfloess
 *
 */
public class FirstFreeSessionMgrFilter extends AbstractFirstFreeSessionMgrFilter implements SessionMgrFilter {
    /**
     * Default constructor.
     */
    public FirstFreeSessionMgrFilter() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SessionMgr findSessionMgr(final Collection<SessionMgr> sessionMgrs) throws Exception {
        return getSessionMgr(0, sessionMgrs);
    }
}
