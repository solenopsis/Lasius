/*
 * Copyright (C) 2014 Scot P. Floess
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.solenopsis.lasius.wsimport.common.security;

import java.util.logging.Logger;
import org.flossware.common.IntegrityUtil;
import org.flossware.util.NetUtil;
import org.solenopsis.lasius.credentials.Credentials;

/**
 * Abstract login result.
 *
 * @author Scot P. Floess
 * @param <V> real login result.
 * @param <S> the security manager for whom we were created.
 */
public abstract class AbstractLoginResult<V, S extends SecurityMgr> implements LoginResult<S> {

    /**
     * Used for logging.
     */
    private final Logger logger;

    /**
     * Result from login.
     */
    private final V loginResult;

    /**
     * The server url.
     */
    private final String serverUrl;

    /**
     * Credentials for login.
     */
    private final Credentials credentials;

    /**
     * The security manager used for login/logout.
     */
    private final S securityMgr;

    /**
     * Constructor to set result of login, credentials used and the security manager who created self.
     *
     * @param loginResult result from login.
     * @param serverUrl   is the full URL from loginResult.
     * @param credentials credentials for login.
     * @param securityMgr security manager used for login/logout.
     *
     * @throws IllegalArgumentException if loginResult, credentials or securityMgr are null.
     */
    protected AbstractLoginResult(final V loginResult, final String serverUrl, final Credentials credentials, final S securityMgr) {
        this.loginResult = IntegrityUtil.ensure(loginResult, "Login result must not be null");
        this.serverUrl = IntegrityUtil.ensure(NetUtil.computeHostUrlAsString(serverUrl) + "/", "The server url must not be blank or null");
        this.credentials = IntegrityUtil.ensure(credentials, "Credentials must not be null");
        this.securityMgr = IntegrityUtil.ensure(securityMgr, "Security manager ust not be null");

        this.logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected final Logger getLogger() {
        return logger;
    }

    /**
     * Return our login result.
     *
     * @return the login result.
     */
    protected V getLoginResult() {
        return loginResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServerUrl() {
        return serverUrl;
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
    public S getSecurityMgr() {
        return securityMgr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invalidate() {
        getSecurityMgr().logout(this);
    }
}
