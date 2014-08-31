package org.solenopsis.lasius.wsimport.common.session.mgr;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.flossware.common.IntegrityUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.common.security.SecurityMgr;
import org.solenopsis.lasius.wsimport.common.session.Session;
import org.solenopsis.lasius.wsimport.common.session.mgr.filter.RoundRobinSessionMgrFilter;
import org.solenopsis.lasius.wsimport.common.session.mgr.filter.SessionMgrFilter;

/**
 *
 * Manages multiple session managers.
 *
 * @author sfloess
 *
 */
public class MultiSessionMgr extends AbstractSessionMgr {

    /**
     * Default session manager filter.
     */
    protected static final SessionMgrFilter DEFAULT_SESSION_MGR_FILTER = new RoundRobinSessionMgrFilter();

    /**
     * The session manager filter.
     */
    private final SessionMgrFilter sessionMgrFilter;

    /**
     * Our session managers.
     */
    private final Map<Credentials, SessionMgr> sessionMgrMap;

    /**
     * Return our session manager filter.
     *
     * @return the session manager filter.
     */
    protected SessionMgrFilter getSessionMgrFilter() {
        return sessionMgrFilter;
    }

    /**
     * Return the map of session managers.
     *
     * @return the map of session managers.
     */
    protected Map<Credentials, SessionMgr> getSessionMgrMap() {
        return sessionMgrMap;
    }

    /**
     * Return our session managers.
     *
     * @return the session managers.
     */
    protected Collection<SessionMgr> getSessionMgrCollection() {
        return getSessionMgrMap().values();
    }

    /**
     * This constructor only allowed to self and sets the session mgr filter.
     *
     * @param sessionMgrFilter is the session manager filter.
     *
     * @throws IllegalArgumentException if sessionMgrFitler is null.
     */
    private MultiSessionMgr(final SessionMgrFilter sessionMgrFilter) {
        IntegrityUtil.ensure(sessionMgrFilter, "Session manager filter cannot be null!");

        this.sessionMgrFilter = sessionMgrFilter;
        this.sessionMgrMap = new HashMap<Credentials, SessionMgr>();
    }

    /**
     * This constructor sets up the session manager filter, and adds session
     * managers to our collection.
     *
     * @param sessionMgrFilter used to find session managers when getSession()
     *                         is called.
     * @param sessionMgrList   is a list of session managers to add.
     *
     * @throws IllegalArgumentException if sessionMgrFilter is null, or
     *                                  sessionMgrList is empty/has null values.
     */
    public MultiSessionMgr(final SessionMgrFilter sessionMgrFilter, final List<SessionMgr> sessionMgrList) {
        this(sessionMgrFilter);

        IntegrityUtil.ensure(sessionMgrList, "Collection of session managers cannot be empty or null!");

        for (final SessionMgr sessionMgr : sessionMgrList) {
            sessionMgrMap.put(sessionMgr.getSession().getLoginResult().getCredentials(), sessionMgr);
        }
    }

    /**
     * This constructor adds session managers to our collection.
     *
     * @param sessionMgrList is a list of session managers to add.
     *
     * @throws IllegalArgumentException if sessionMgrList is empty/has null
     *                                  values.
     */
    public MultiSessionMgr(final List<SessionMgr> sessionMgrList) {
        this(DEFAULT_SESSION_MGR_FILTER, sessionMgrList);
    }

    /**
     * This constructor sets up the session manager filter, and adds session
     * managers to our collection.
     *
     * @param sessionMgrFilter used to find session managers when getSession()
     *                         is called.
     * @param sessionMgrs      is an array of session managers to add.
     *
     * @throws IllegalArgumentException if sessionMgrFilter is null, or
     *                                  sessionMgrs is empty/has null values.
     */
    public MultiSessionMgr(final SessionMgrFilter sessionMgrFilter, final SessionMgr[] sessionMgrs) {
        this(sessionMgrFilter, Arrays.asList(sessionMgrs));
    }

    /**
     * This constructor adds session managers to our collection.
     *
     * @param sessionMgrs is an array of session managers to add.
     *
     * @throws IllegalArgumentException if sessionMgrs is empty/has null values.
     */
    public MultiSessionMgr(final SessionMgr[] sessionMgrs) {
        this(DEFAULT_SESSION_MGR_FILTER, sessionMgrs);
    }

    /**
     * This constructor sets up the session manager filter, credentials and the
     * security web service for logins.
     *
     * @param sessionMgrFilter used to find session managers when getSession()
     *                         is called.
     * @param credentialsItr   the credentials for login.
     * @param securityWebSvc   called on login.
     *
     * @throws IllegalArgumentException if sessionMgrFilter is null, credentials
     *                                  is empty or has null values or
     *                                  securityWebSvc is null.
     */
    public MultiSessionMgr(final SessionMgrFilter sessionMgrFilter, final Iterator<Credentials> credentialsItr, final SecurityMgr securityWebSvc) {
        this(sessionMgrFilter);

        IntegrityUtil.ensure(credentialsItr, "Credentials cannot be empty or null!");
        IntegrityUtil.ensure(securityWebSvc, "Security web service cannot be null!");

        while (credentialsItr.hasNext()) {
            final Credentials credentials = credentialsItr.next();
            sessionMgrMap.put(credentials, new SingleSessionMgr(credentials, securityWebSvc));
        }
    }

    /**
     * This constructor sets up the session manager filter, credentials and the
     * security web service for logins.
     *
     * @param sessionMgrFilter used to find session managers when getSession()
     *                         is called.
     * @param credentials      the credentials for login.
     * @param securityWebSvc   called on login.
     *
     * @throws IllegalArgumentException if sessionMgrFilter is null, credentials
     *                                  is empty or has null values or
     *                                  securityWebSvc is null.
     */
    public MultiSessionMgr(final SessionMgrFilter sessionMgrFilter, final Credentials[] credentials, final SecurityMgr securityWebSvc) {
        this(sessionMgrFilter, Arrays.asList(credentials).iterator(), securityWebSvc);
    }

    /**
     * This constructor sets up the session manager filter, credentials and the
     * security web service for logins.
     *
     * @param sessionMgrFilter used to find session managers when getSession()
     *                         is called.
     * @param credentials      the credentials for login.
     * @param securityWebSvc   called on login.
     *
     * @throws IllegalArgumentException if sessionMgrFilter is null, credentials
     *                                  is empty or has null values or
     *                                  securityWebSvc is null.
     */
    public MultiSessionMgr(final SessionMgrFilter sessionMgrFilter, final Collection<Credentials> credentials, final SecurityMgr securityWebSvc) {
        this(sessionMgrFilter, IntegrityUtil.ensure(credentials, "Cannot have empty/null credentials").iterator(), securityWebSvc);
    }

    /**
     * This constructor sets up the credentials and the security web service for
     * logins.
     *
     * @param credentials    the credentials for login.
     * @param securityWebSvc called on login.
     *
     * @throws IllegalArgumentException if credentials is empty or has null
     *                                  values or securityWebSvc is null.
     */
    public MultiSessionMgr(final Credentials[] credentials, final SecurityMgr securityWebSvc) {
        this(DEFAULT_SESSION_MGR_FILTER, credentials, securityWebSvc);
    }

    /**
     * This constructor sets up the credentials and the security web service for
     * logins.
     *
     * @param credentials    the credentials for login.
     * @param securityWebSvc called on login.
     *
     * @throws IllegalArgumentException if credentials is empty or has null
     *                                  values or securityWebSvc is null.
     */
    public MultiSessionMgr(final Collection<Credentials> credentials, final SecurityMgr securityWebSvc) {
        this(DEFAULT_SESSION_MGR_FILTER, IntegrityUtil.ensure(credentials, "Cannot have empty/null credentials"), securityWebSvc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session getSession() {
        final Session retVal = getSessionMgrFilter().getSessionMgr(getSessionMgrCollection()).getSession();
        getLogger().log(Level.FINE, "Returning [{0}] -> [{1}]", new Object[] {retVal.getLoginResult().getUserId(), retVal.getLoginResult().getCredentials().getUserName()});

        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session resetSession(final Session oldSession) {
        return getSessionMgrMap().get(oldSession.getLoginResult().getCredentials()).resetSession(oldSession);
    }
}
