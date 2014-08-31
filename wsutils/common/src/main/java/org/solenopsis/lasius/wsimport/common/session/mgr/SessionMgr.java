package org.solenopsis.lasius.wsimport.common.session.mgr;

import org.solenopsis.lasius.wsimport.common.session.Session;

/**
 *
 * Interface for session management.
 *
 * @author sfloess
 *
 */
public interface SessionMgr {

    /**
     * Return a session.
     *
     * @return a session.
     */
    public Session getSession();

    /**
     * Reset a session.
     *
     * @param oldSession the session to reset.
     *
     * @return the reset session.
     */
    public Session resetSession(final Session oldSession);
}
