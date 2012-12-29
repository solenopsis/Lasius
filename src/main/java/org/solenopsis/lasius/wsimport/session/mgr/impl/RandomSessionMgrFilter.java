package org.solenopsis.lasius.wsimport.session.mgr.impl;

import java.util.Collection;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgrFilter;

/**
 *
 * Chooses a session manager from a collection randomly.
 *
 * @author sfloess
 *
 */
public class RandomSessionMgrFilter extends AbstractSessionMgrFilter implements SessionMgrFilter {
    /**
     * Default constructor.
     */
    public RandomSessionMgrFilter() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionMgr getSessionMgr(final Collection<SessionMgr> sessionMgrs) {
        return getSessionMgr((int) (Math.random() * sessionMgrs.size()), sessionMgrs);
    }
}
