package org.solenopsis.lasius.wsimport.common.session.mgr;

import java.util.concurrent.atomic.AtomicReference;
import org.flossware.common.IntegrityUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.common.security.SecurityMgr;
import org.solenopsis.lasius.wsimport.common.session.DefaultSession;
import org.solenopsis.lasius.wsimport.common.session.Session;

/**
 *
 * Manages single session.
 *
 * @author sfloess
 *
 */
public class SingleSessionMgr extends AbstractSessionMgr {

    /**
     * Our credentials.
     */
    private final Credentials credentials;

    /**
     * Our security web service.
     */
    private final SecurityMgr securityWebSvc;

    /**
     * Our current session.
     */
    private final AtomicReference<Session> session;

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
    protected SecurityMgr getSecurityWebSvc() {
        return securityWebSvc;
    }

    /**
     * This constructor sets up the credentials and security web service.
     *
     * @param credentials    the credentials to use when logging in.
     * @param securityWebSvc the security web service.
     *
     * @throws IllegalArgumentException if credentials or securityWebSvc are
     *                                  null.
     */
    public SingleSessionMgr(final Credentials credentials, final SecurityMgr securityWebSvc) {
        IntegrityUtil.ensure(securityWebSvc, "Security web service cannot be null!");

        this.credentials = credentials;
        this.securityWebSvc = securityWebSvc;
        this.session = new AtomicReference(new DefaultSession(securityWebSvc.login(credentials)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session getSession() {
        return session.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session resetSession(final Session oldSession) {
        session.set(new DefaultSession(getSecurityWebSvc().login(getCredentials())));

        return session.get();
    }
}
