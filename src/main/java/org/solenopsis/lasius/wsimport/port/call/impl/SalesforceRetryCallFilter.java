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

    public SalesforceRetryCallFilter(final SessionMgr sessionMgr) {
        this.sessionMgr = sessionMgr;
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    public boolean isToRetryCall(final Call<Session> call, final Exception failure) throws Exception {
        if (SalesforceWebServiceUtil.isInvalidSessionId(failure) || SalesforceWebServiceUtil.containsIOException(failure)) {
            getSessionMgr().resetSession(call.getValue());

            return true;
        }

        return false;
    }
}
