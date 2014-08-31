package org.solenopsis.lasius.wsimport.common.session.mgr.filter;

import java.util.Collection;
import org.solenopsis.lasius.wsimport.common.session.mgr.SessionMgr;

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
    protected SessionMgr findSessionMgr(final Collection<SessionMgr> sessionMgrs) {
        return getSessionMgr(0, sessionMgrs);
    }
}
