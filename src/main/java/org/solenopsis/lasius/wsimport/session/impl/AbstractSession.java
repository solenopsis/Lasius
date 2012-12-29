package org.solenopsis.lasius.wsimport.session.impl;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;
import org.flossware.util.ParameterUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.session.Session;

/**
 * Abstract base class for sessions.
 *
 * @param <V> the login result type.
 *
 * @author sfloess
 */
public abstract class AbstractSession<V> implements Session {
    /**
     * Our logger.
     */
    private final Logger logger;

    /**
     * Our credentials.
     */
    private final Credentials credentials;

    /**
     * Our login result.
     */
    private final V loginResult;

    /**
     * The server URL for this session.
     */
    private final String serverUrl;

    /**
     * Our semaphore - used for concurrent SFDC access.
     */
    private final Semaphore semaphore;

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
     * Return the login result.
     *
     * @return the login result.
     */
    protected final V getLoginResult() {
        return loginResult;
    }

    /**
     * Set the credentials for this session.
     *
     * @param credentials are the credentials for this session.
     * @param serverUrl is the URL of the server.
     *
     * @throws IllegalArgumentException if credentials is null, loginResult is null or serverUrl is empty/null.
     */
    protected AbstractSession(final Credentials credentials, final V loginResult, final String serverUrl) {
        ParameterUtil.ensureParameter(credentials, "Cannot have null credentials!");
        ParameterUtil.ensureParameter(loginResult, "Cannot have a null login result!");
        ParameterUtil.ensureParameter(serverUrl, "Cannot have a null/empty server url!");

        this.logger      = Logger.getLogger(getClass().getName());
        this.credentials = credentials;
        this.loginResult = loginResult;
        this.serverUrl   = serverUrl;
        this.semaphore   = new Semaphore(DEFAULT_CONCURRENT_ACCESS);
        this.id          = new Long(System.currentTimeMillis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Semaphore getSemaphore() {
        return semaphore;
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
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServerUrl() {
        return serverUrl;
    }
}