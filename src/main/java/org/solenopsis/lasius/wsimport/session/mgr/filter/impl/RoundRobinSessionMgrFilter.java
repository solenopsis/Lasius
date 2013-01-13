package org.solenopsis.lasius.wsimport.session.mgr.filter.impl;

import java.util.Collection;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;
import org.solenopsis.lasius.wsimport.session.mgr.filter.SessionMgrFilter;

/**
 *
 * Chooses a session manager from a collection in a round robin fashion.
 *
 * @author sfloess
 *
 */
public class RoundRobinSessionMgrFilter extends AbstractSessionMgrFilter implements SessionMgrFilter {
    /**
     * Our last index into a collection of session managers.
     */
    private int lastIndex;

    /**
     * Default constructor.
     */
    public RoundRobinSessionMgrFilter() {
        lastIndex = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionMgr getSessionMgr(final Collection<SessionMgr> sessionMgrs) {
        if (lastIndex++ > sessionMgrs.size()) {
            lastIndex = 0;
        }

        return (SessionMgr) sessionMgrs.toArray()[lastIndex];
    }
}
