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
