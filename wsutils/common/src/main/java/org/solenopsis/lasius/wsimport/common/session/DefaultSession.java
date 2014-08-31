package org.solenopsis.lasius.wsimport.common.session;

import org.solenopsis.lasius.wsimport.common.security.LoginResult;

/**
 *
 * Denotes an SFDC session.
 *
 * @author sfloess
 *
 */
public class DefaultSession extends AbstractSession {

    /**
     * This constructor sets the login result.
     *
     * @param loginResult the login result from SFDC.
     */
    public DefaultSession(final LoginResult loginResult) {
        super(loginResult);
    }
}
