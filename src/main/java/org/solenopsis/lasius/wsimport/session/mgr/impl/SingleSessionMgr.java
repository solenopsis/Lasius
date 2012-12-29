package org.solenopsis.lasius.wsimport.session.mgr.impl;

import org.flossware.util.ParameterUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.security.SecurityWebSvc;
import org.solenopsis.lasius.wsimport.session.security.impl.EnterpriseSecurityWebSvc;

/**
 *
 * Manages single session.
 *
 * @author sfloess
 *
 */
public class SingleSessionMgr extends AbstractSessionMgr {
    /**
     * Our default security web service.
     */
    protected static final SecurityWebSvc DEFAULT_SECURITY_WEB_SVC = new EnterpriseSecurityWebSvc();

    /**
     * Our credentials.
     */
    private final Credentials credentials;

    /**
     * Our security web service.
     */
    private final SecurityWebSvc securityWebSvc;

    /**
     * Our current session.
     */
    private Session session;

    /**
     * Return the credentials.
     *
     * @return the credentials.
     */
    protected Credentials getCredentials() {
        return credentials;
    }

    /**
     * Return the security web service.
     *
     * @return the security web service.
     */
    protected SecurityWebSvc getSecurityWebSvc() {
        return securityWebSvc;
    }

    /**
     * This constructor sets up the credentials and security web service.
     *
     * @param credentials the credentials to use when logging in.
     * @param securityWebSvc the security web service.
     *
     * @throws IllegalArgumentException if credentials or securityWebSvc are null.
     */
    public SingleSessionMgr(final Credentials credentials, final SecurityWebSvc securityWebSvc) {
        ParameterUtil.ensureParameter(securityWebSvc, "Security web service cannot be null!");

        this.credentials    = credentials;
        this.securityWebSvc = securityWebSvc;
    }

    /**
     * This constructor sets up our credentials.  We will default to using
     * the enterprise security web service.
     *
     * @param credentials the credentials to use when logging in.
     *
     * @throws IllegalArgumentException if credentials is null.
     */
    public SingleSessionMgr(final Credentials credentials) {
        this(credentials, DEFAULT_SECURITY_WEB_SVC);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Session getSession() throws Exception {
        if (session != null) {
            return session;
        }

        session = getSecurityWebSvc().login(getCredentials());

        return session;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Session resetSession(final Session oldSession) throws Exception {
        // If the Id's are not the same, its already been reset...
        if (!session.getId().equals(oldSession.getId())) {
            return session;
        }

        this.session = null;

        return getSession();
    }
}
