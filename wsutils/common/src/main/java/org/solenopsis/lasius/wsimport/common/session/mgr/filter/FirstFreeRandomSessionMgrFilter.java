package org.solenopsis.lasius.wsimport.common.session.mgr.filter;

import java.util.Collection;
import org.solenopsis.lasius.wsimport.common.session.mgr.SessionMgr;

/**
 *
 * Will choose a random session manager should there not be a first one free.
 *
 * @author sfloess
 *
 */
public class FirstFreeRandomSessionMgrFilter extends AbstractFirstFreeSessionMgrFilter implements SessionMgrFilter {

    /**
     * Our random session manager filter.
     */
    private final RandomSessionMgrFilter randomSessionMgrFilter;

    /**
     * Return our random session manager filter.
     *
     * @return our random session manager filter.
     */
    protected RandomSessionMgrFilter getRandomSessionMgrFilter() {
        return randomSessionMgrFilter;
    }

    /**
     * Default constructor.
     */
    public FirstFreeRandomSessionMgrFilter() {
        randomSessionMgrFilter = new RandomSessionMgrFilter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SessionMgr findSessionMgr(final Collection<SessionMgr> sessionMgrs) {
        return getRandomSessionMgrFilter().getSessionMgr(sessionMgrs);
    }
}
