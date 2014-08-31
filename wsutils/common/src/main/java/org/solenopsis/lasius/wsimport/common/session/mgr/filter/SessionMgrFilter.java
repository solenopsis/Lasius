package org.solenopsis.lasius.wsimport.common.session.mgr.filter;

import java.util.Collection;
import org.solenopsis.lasius.wsimport.common.session.mgr.SessionMgr;

/**
 *
 * Selects a session manager from a collection of session managers.
 *
 * @author sfloess
 *
 */
public interface SessionMgrFilter {

    /**
     * Chooses a session manager from a list of session managers.
     *
     * @param sessionMgrs the collection of session managers to chose from.
     *
     * @return a session manager.
     */
    public SessionMgr getSessionMgr(Collection<SessionMgr> sessionMgrs);
}
