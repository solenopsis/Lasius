package org.solenopsis.lasius.wsimport.common.session;

import org.solenopsis.lasius.wsimport.common.security.LoginResult;

/**
 *
 * Denotes an SFDC session.
 *
 * @author sfloess
 *
 */
public interface Session {

    /**
     * Default number of concurrent calls to SFDC.
     */
    public static final int DEFAULT_CONCURRENT_ACCESS = 10;

    /**
     * Attempt to lock a session.
     */
    public void lock();

    /**
     * Unlock the session.
     */
    public void unlock();

    /**
     * Return the number of locks available.
     *
     * @return the number of locks available.
     */
    public int getRemainingLocks();

    /**
     * Return a session id. This is not the SFDC session id.
     *
     * @return a session id.
     */
    public Object getId();

    /**
     * Return the results of login.
     *
     * @return our login results.
     */
    public LoginResult getLoginResult();
}
