package org.solenopsis.lasius.wsimport.common.util;

import java.lang.reflect.Method;
import java.util.logging.Level;
import org.flossware.common.IntegrityUtil;
import org.flossware.wsimport.service.WebService;
import org.flossware.wsimport.service.decorator.AbstractPortInvocationHandler;
import org.solenopsis.lasius.util.ExceptionUtil;
import org.solenopsis.lasius.wsimport.common.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.common.session.Session;
import org.solenopsis.lasius.wsimport.common.session.mgr.SessionMgr;

/**
 *
 * The purpose of this class is
 *
 * @author sfloess
 *
 */
public class SalesforceWebServicePortInvoker extends AbstractPortInvocationHandler {

    /**
     * Default pause time in millis.
     */
    final static long DEFAULT_PAUSE_TIME = 2000;

    /**
     * The session manager.
     */
    private final SessionMgr sessionMgr;

    /**
     * The type of web service.
     */
    private final WebServiceTypeEnum webServiceType;

    /**
     * Pauses execution.
     */
    void pause(final Object lock) {
        try {
            getLogger().log(Level.INFO, "Pausing current thread before retrying...");

            synchronized (lock) {
                lock.wait(DEFAULT_PAUSE_TIME);
            }
        } catch (final InterruptedException ex) {
            getLogger().log(Level.WARNING, "Trouble pausing current thread...", ex);
        }
    }

    /**
     * Return the session manager.
     *
     * @return the session manager.
     */
    protected SessionMgr getSessionMgr() {
        return sessionMgr;
    }

    /**
     * Return the type of web service.
     *
     * @return the type of web service.
     */
    protected WebServiceTypeEnum getWebServiceType() {
        return webServiceType;
    }

    /**
     * Can we re
     *
     * @param totalCalls
     *
     * @return
     */
    protected boolean isCallable(int totalCalls) {
        return totalCalls < SalesforceWebServiceUtil.MAX_RETRIES;
    }

    /**
     * When an exception happens on call, this method will handle the exception.
     *
     * @param callFailure the exception that arose when calling SFDC.
     * @param method      the method being called when the failure arose.
     * @param session     the session being used when calling SFDC.
     *
     * @throws Throwable if the exception cannot be handled.
     */
    protected void handleException(final Throwable callFailure, final Method method, final Session session) throws Throwable {
        if (ExceptionUtil.isReloginException(callFailure)) {
            getLogger().log(Level.INFO, "Received a relogin exception when calling [{0}]", method.getName());

            getSessionMgr().resetSession(session);
        } else if (ExceptionUtil.isRetryException(callFailure)) {
            getLogger().log(Level.WARNING, "Web service retry encountered calling [{0}] - [{1}]", new Object[]{method.getName(), callFailure.getLocalizedMessage()});

            pause(new byte[0]);
        } else {
            getLogger().log(Level.FINE, "Trouble calling [{0}] - [{1}]", new Object[]{method.getName(), callFailure.getLocalizedMessage()});

            throw callFailure;
        }
    }

    /**
     * This constructor sets the sessionMgr to use, the type of web service and the class of the web service being used.
     *
     * @param sessionMgr     manages our sessions with SFDC.
     * @param webServiceType the type of web service being used.
     *
     * @throws IllegalArgumentException if sessionMgr, webServiceType or serviceClass are null.
     */
    public SalesforceWebServicePortInvoker(final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType) {
        this.sessionMgr = IntegrityUtil.ensure(sessionMgr, "Cannot have a null session mgr!");
        this.webServiceType = IntegrityUtil.ensure(webServiceType, "Cannot have a null web service type!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(final WebService webService, final Object proxy, final Method method, final Object[] args) throws Throwable {
        int totalCalls = 0;

        getLogger().log(Level.FINE, "Calling [{0}]", method.getName());

        Session session = null;

        while (isCallable(totalCalls++)) {
            try {
                session = getSessionMgr().getSession();

                return method.invoke(SalesforceWebServiceUtil.createPort(session, getWebServiceType(), webService), args);
            } catch (final Exception callFailure) {
                handleException(callFailure, method, session);
            }
        }

        throw new IllegalStateException("Should have returned a value!");
    }
}
