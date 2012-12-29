package org.solenopsis.lasius.wsimport.session.mgr.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.flossware.util.ParameterUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgrFilter;
import org.solenopsis.lasius.wsimport.session.security.SecurityWebSvc;

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
     * Default security web service - we'll use the default frmo
     * the single session manager.
     */
    protected static final SecurityWebSvc DEFAULT_SECURITY_WEB_SVC = SingleSessionMgr.DEFAULT_SECURITY_WEB_SVC;

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
        ParameterUtil.ensureParameter(sessionMgrFilter, "Session manager filter cannot be null!");

        this.sessionMgrFilter = sessionMgrFilter;
        this.sessionMgrMap    = new HashMap<Credentials, SessionMgr>();
    }

    /**
     * This constructor sets up the session manager filter, and adds session managers to our collection.
     *
     * @param sessionMgrFilter used to find session managers when getSession() is called.
     * @param sessionMgrList is a list of session managers to add.
     *
     * @throws IllegalArgumentException if sessionMgrFilter is null, or sessionMgrList is empty/has null values.
     */
    public MultiSessionMgr(final SessionMgrFilter sessionMgrFilter, final List<SessionMgr> sessionMgrList) throws Exception{
        this(sessionMgrFilter);

        ParameterUtil.ensureParameter(sessionMgrList, "Collection of session managers cannot be empty or null!");

        for(final SessionMgr sessionMgr : sessionMgrList) {
            sessionMgrMap.put(sessionMgr.getSession().getCredentials(), sessionMgr);
        }
    }

    /**
     * This constructor adds session managers to our collection.
     *
     * @param sessionMgrList is a list of session managers to add.
     *
     * @throws IllegalArgumentException if sessionMgrList is empty/has null values.
     */
    public MultiSessionMgr(final List<SessionMgr> sessionMgrList) throws Exception{
        this(DEFAULT_SESSION_MGR_FILTER, sessionMgrList);
    }

    /**
     * This constructor sets up the session manager filter, and adds session managers to our collection.
     *
     * @param sessionMgrFilter used to find session managers when getSession() is called.
     * @param sessionMgrs is an array of session managers to add.
     *
     * @throws IllegalArgumentException if sessionMgrFilter is null, or sessionMgrs is empty/has null values.
     */
    public MultiSessionMgr(final SessionMgrFilter sessionMgrFilter, final SessionMgr[] sessionMgrs) throws Exception {
        this(sessionMgrFilter, Arrays.asList(sessionMgrs));
    }

    /**
     * This constructor adds session managers to our collection.
     *
     * @param sessionMgrs is an array of session managers to add.
     *
     * @throws IllegalArgumentException if sessionMgrs is empty/has null values.
     */
    public MultiSessionMgr(final SessionMgr[] sessionMgrs) throws Exception {
        this(DEFAULT_SESSION_MGR_FILTER, sessionMgrs);
    }

    /**
     * This constructor sets up the session manager filter, credentials and the security web service for logins.
     *
     * @param sessionMgrFilter used to find session managers when getSession() is called.
     * @param credentials the credentials for login.
     * @param securityWebSvc called on login.
     *
     * @throws IllegalArgumentException if sessionMgrFilter is null, credentials is empty or has null values or
     *         securityWebSvc is null.
     */
    public MultiSessionMgr(final SessionMgrFilter sessionMgrFilter, final Credentials[] credentials, final SecurityWebSvc securityWebSvc) {
        this(sessionMgrFilter);

        ParameterUtil.ensureParameter(credentials, "Credentials cannot be empty or null!");
        ParameterUtil.ensureParameter(securityWebSvc, "Security web service cannot be null!");

        for(final Credentials credential : credentials) {
            sessionMgrMap.put(credential, new SingleSessionMgr(credential, securityWebSvc));
        }
    }

    /**
     * This constructor sets up the session manager filter, credentials and the security web service for logins.
     *
     * @param sessionMgrFilter used to find session managers when getSession() is called.
     * @param credentials the credentials for login.
     *
     * @throws IllegalArgumentException if sessionMgrFilter is null, or credentials are null.
     */
    public MultiSessionMgr(final SessionMgrFilter sessionMgrFilter, final Credentials[] credentials) {
        this(sessionMgrFilter, credentials, DEFAULT_SECURITY_WEB_SVC);
    }

    /**
     * This constructor sets up the credentials and the security web service for logins.
     *
     * @param credentials the credentials for login.
     * @param securityWebSvc called on login.
     *
     * @throws IllegalArgumentException if credentials is empty or has null values or
     *         securityWebSvc is null.
     */
    public MultiSessionMgr(final Credentials[] credentials, final SecurityWebSvc securityWebSvc) {
        this(DEFAULT_SESSION_MGR_FILTER, credentials, securityWebSvc);
    }

    /**
     * This constructor sets up the credentials and the security web service for logins.
     *
     * @param credentials the credentials for login.
     * @param securityWebSvc called on login.
     *
     * @throws IllegalArgumentException if credentials is empty or has null values or
     *         securityWebSvc is null.
     */
    public MultiSessionMgr(final Credentials[] credentials) {
        this(DEFAULT_SESSION_MGR_FILTER, credentials, DEFAULT_SECURITY_WEB_SVC);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session getSession() throws Exception {
        return getSessionMgrFilter().getSessionMgr(getSessionMgrCollection()).getSession();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session resetSession(final Session oldSession) throws Exception {
        return getSessionMgrMap().get(oldSession.getCredentials()).resetSession(oldSession);
    }
}
