package org.solenopsis.lasius.wsimport.session.mgr;

import org.solenopsis.lasius.wsimport.session.Session;

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
     *
     * @throws Exception if there is a problem retrieving a session.
     */
    public Session getSession() throws Exception;

    /**
     * Reset a session.
     *
     * @param oldSession the session to reset.
     *
     * @return the reset session.
     *
     * @throws Exception if any problems arise calling the session.
     */
    public Session resetSession(final Session oldSession) throws Exception;
}
