package org.solenopsis.lasius.wsimport.common.session;

import java.util.logging.Logger;
import org.flossware.common.IntegrityUtil;
import org.solenopsis.lasius.wsimport.common.security.LoginResult;

/**
 * Abstract base class for sessions.
 *
 * @author sfloess
 */
public abstract class AbstractSession implements Session {

    /**
     * Our logger.
     */
    private final Logger logger;

    /**
     * Our login result.
     */
    private final LoginResult loginResult;

    /**
     * Our id.
     */
    private final Long id;

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected final Logger getLogger() {
        return logger;
    }

    /**
     * Set the login results for this session.
     *
     * @param loginResult the result of login.
     *
     * @throws IllegalArgumentException if loginResult is null.
     */
    protected AbstractSession(final LoginResult loginResult) {
        this.logger = Logger.getLogger(getClass().getName());
        this.loginResult = IntegrityUtil.ensure(loginResult, "Cannot have a null login result!");
        this.id = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginResult getLoginResult() {
        return loginResult;
    }
}
