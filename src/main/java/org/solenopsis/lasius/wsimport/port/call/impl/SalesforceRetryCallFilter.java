package org.solenopsis.lasius.wsimport.port.call.impl;

import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.RetryCallFilter;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Allows retries to Salesforce calls if we have an invalid session id.
 *
 * @author sfloess
 *
 */
public class SalesforceRetryCallFilter implements RetryCallFilter<Session> {
    private final SessionMgr sessionMgr;

    protected SessionMgr getSessionMgr() {
        return sessionMgr;
    }

    /**
     * If failure represents a reset session, return true else false.
     *
     * @param failure is the exception that was raised making a call.
     *
     * @return true if failure represents a reset (invalid session id or IOException)...
     */
    protected boolean isResetSession(final Exception failure) {
        return SalesforceWebServiceUtil.isInvalidSessionId(failure) || SalesforceWebServiceUtil.containsIOException(failure);
    }

    public SalesforceRetryCallFilter(final SessionMgr sessionMgr) {
        this.sessionMgr = sessionMgr;
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    public boolean isToRetryCall(final Call<Session> call, final Exception failure) throws Exception {
        // We will reset the session when we encounter an invalid session id or an IOException.
        // For either, we can retry the call.
        if (isResetSession(failure)) {
            getSessionMgr().resetSession(call.getValue());

            return true;
        }

        // If the server is unavailable, we will want to
        // retr the call.
        if (SalesforceWebServiceUtil.isServerUnavailable(failure)) {
            return true;
        }

        // Anything else, no retry.
        return false;
    }
}
