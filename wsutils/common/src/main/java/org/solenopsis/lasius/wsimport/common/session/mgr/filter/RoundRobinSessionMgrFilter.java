package org.solenopsis.lasius.wsimport.common.session.mgr.filter;

import java.util.Collection;
import org.solenopsis.lasius.wsimport.common.session.mgr.SessionMgr;

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
     * Returns the next index to use. If we go over the total number of session
     * managers, we will return 0.
     *
     * @param sessionMgrs the collection of session managers.
     *
     * @return the next index to use.
     */
    protected synchronized int getNextIndex(final Collection<SessionMgr> sessionMgrs) {
        if (++lastIndex < sessionMgrs.size()) {
            return lastIndex;
        }

        lastIndex = 0;

        return 0;
    }

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
        return (SessionMgr) sessionMgrs.toArray()[getNextIndex(sessionMgrs)];
    }
}
