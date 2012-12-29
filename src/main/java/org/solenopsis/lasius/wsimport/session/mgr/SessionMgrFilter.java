package org.solenopsis.lasius.wsimport.session.mgr;

import java.util.Collection;

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
     *
     * @throws Exception if there are any problems getting a session manager.
     */
    public SessionMgr getSessionMgr(Collection<SessionMgr> sessionMgrs) throws Exception;
}
