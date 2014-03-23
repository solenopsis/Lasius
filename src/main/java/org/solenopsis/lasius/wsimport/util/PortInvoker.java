package org.solenopsis.lasius.wsimport.util;

import java.util.logging.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.xml.ws.Service;
import java.util.logging.Level;

import org.flossware.util.ParameterUtil;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * The purpose of this class is
 *
 * @author sfloess
 *
 */
public class PortInvoker implements InvocationHandler {
    /**
     * Used for logging.
     */
    private final Logger logger;

    /**
     * The session manager.
     */
    private final SessionMgr sessionMgr;

    /**
     * The type of web service.
     */
    private final WebServiceTypeEnum webServiceType;

    /**
     * The web service.
     */
    private final Class<? extends Service> serviceClass;

    /**
     * The name of the web service.
     */
    private final String name;

    /**
     * Return the logger.
     * 
     * @return the logger.
     */
    protected Logger getLogger() {
        return logger;
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
     * Return the web service.
     *
     * @return the web service.
     */
    protected Class<? extends Service> getServiceClass() {
        return serviceClass;
    }

    /**
     * Return the name of the web service.
     *
     * @return the name of the web service.
     */
    protected String getName() {
        return name;
    }

    /**
     * Can we re
     * @param totalCalls
     * @return 
     */
    protected boolean isCallable(int totalCalls) {
        return totalCalls < SalesforceWebServiceUtil.MAX_RETRIES;
    }

    protected void unlock(final Session session) {
        if (null == session) {
            return;
        }

        try {
            session.unlock();
        } catch(final Exception exception) {
            getLogger().log(Level.WARNING, "Could not unlock session", exception);
        }
    }

    protected void handleException(final Throwable callFailure, final Method method, final Session session) throws Throwable {
        if (!SalesforceWebServiceUtil.isReloginException(callFailure)) {
            getLogger().log(Level.FINE, "Trouble calling [{0}] - [{1}]", new Object[]{method.getName(), callFailure.getLocalizedMessage()});

            throw callFailure;
        }

        getLogger().log(Level.INFO, "Received a relogin exception when calling [{0}]", method.getName());

        getSessionMgr().resetSession(session);
    }

    protected Object doInvoke(Object o, Method method, Object[] args) throws Throwable {
        Session session = null;

        try {
            session = getSessionMgr().getSession();

            session.lock();

            return method.invoke(SalesforceWebServiceUtil.createPort(session, getWebServiceType(), getServiceClass(), getName()), args);
        } catch (final Exception callFailure) {
            handleException(callFailure, method, session);
        } finally {
            unlock(session);
        }

        getLogger().log(Level.WARNING, "We should never reach this place in the code - raising an exception!");

        throw new IllegalStateException("Problems calling web service: [" + getServiceClass().getName() + "]");
    }

    public PortInvoker (final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final String name) {
        this.sessionMgr     = ParameterUtil.ensureParameter(sessionMgr,     "Cannot have a null session mgr!");
        this.webServiceType = ParameterUtil.ensureParameter(webServiceType, "Cannot have a null web service type!");
        this.serviceClass   = ParameterUtil.ensureParameter(serviceClass,   "Cannot have a null service class!");
        this.name           = ParameterUtil.ensureParameter(name,           "Cannot have a null/empty name!");

        this.logger = Logger.getLogger(getClass().getName());
    }

    @Override
    public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
        int totalCalls = 0;

        getLogger().log(Level.WARNING, "Calling [{0}]", method.getName());

        while (isCallable(totalCalls++)) {
           return doInvoke(obj, method, args);
        }

        throw new IllegalStateException("Should have returned a value!");
    }

}
