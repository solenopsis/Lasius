package org.solenopsis.lasius.wsimport.common.util;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
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
    final static int DEFAULT_PAUSE_TIME = 5000;

    final static Random RANDOM_PAUSE = new Random(DEFAULT_PAUSE_TIME);

    /**
     * The session manager.
     */
    private final SessionMgr sessionMgr;

    /**
     * The type of web service.
     */
    private final WebServiceTypeEnum webServiceType;

    static final int DEFAULT_TOTAL_LOCKS = Integer.MAX_VALUE;

    private static final ConcurrentHashMap<Class, Semaphore> METHOD_MAP = new ConcurrentHashMap<>();

    private final AtomicReference port;

    /**
     * Can allow fine tuning when calling web service asynchronously...
     *
     * @param totalLocks
     */
    public static void setSemaphoreLimit(final Class klass, final int totalLocks) {
        System.out.println("ADDING [" + klass.getName() + "]");
        METHOD_MAP.put(klass, new Semaphore(totalLocks));
        System.out.println(METHOD_MAP);
    }

    /**
     * Lock down the proxy...
     */
    static boolean lock(final Class klass) throws Exception {
        System.out.println(METHOD_MAP);
        if (!METHOD_MAP.contains(klass)) {
            System.out.println("NOT FOUND [" + klass.getName() + "]");
            System.out.println(METHOD_MAP);
            return false;
        }

        System.out.println("LOCK FOUND [" + klass.getName() + "]");

        METHOD_MAP.get(klass).acquire();
        return true;
    }

    /**
     * Lock down the proxy.
     */
    static void lock(final Object proxy) throws Exception {
        for (final Class intr : proxy.getClass().getInterfaces()) {
            System.out.println("Examining [" + intr.getName() + "]");
            if (lock(intr)) {
                return;
            }
        }
    }

    /**
     * Release the proxy...
     */
    static boolean unlock(final Class klass) throws Exception {
        System.out.println(METHOD_MAP);
        if (!METHOD_MAP.contains(klass)) {
            return false;
        }

        System.out.println("UNLOCK FOUND [" + klass.getName() + "]");

        METHOD_MAP.get(klass).release();
        return true;
    }

    /**
     * Release the proxy.
     */
    static void unlock(final Object proxy) throws Exception {
        for (final Class intr : proxy.getClass().getInterfaces()) {
            if (unlock(intr)) {
                return;
            }
        }
    }

    /**
     * Pauses execution.
     */
    void pause(final Object lock, final int totalCalls) {
        try {
            final long waitTime = RANDOM_PAUSE.nextInt(DEFAULT_PAUSE_TIME);

            getLogger().log(Level.INFO, "Pausing current thread [{0} ms] before retrying attempt [#{1}]...", new Object[]{waitTime, totalCalls});

            synchronized (lock) {
                lock.wait(waitTime);
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
    protected void handleException(final Throwable callFailure, final WebService webService, final Object proxy, final Method method, final Session session, final int totalCalls) throws Throwable {
//        unlock(proxy);
        if (ExceptionUtil.isReloginException(callFailure)) {
            getLogger().log(Level.INFO, "Received a relogin exception when calling [{0}] - retry attempt [#{1}]", new Object[]{method.getName(), totalCalls});

            getSessionMgr().resetSession(session);
            port.set(SalesforceWebServiceUtil.createPort(session, getWebServiceType(), webService));
        } else if (ExceptionUtil.isRetryException(callFailure)) {
            getLogger().log(Level.WARNING, "Web service retry encountered calling [{0}] - retry attempt [#{1}]", new Object[]{method.getName(), totalCalls});

            pause(new byte[0], totalCalls);
        } else {
            getLogger().log(Level.FINE, "Trouble calling [" + method.getName() + "]", callFailure);

            throw callFailure;
        }
    }

    /**
     * This constructor sets the sessionMgr to use, the type of web service and the class of the web service being used.
     *
     * @param <P>
     * @param sessionMgr     manages our sessions with SFDC.
     * @param webServiceType the type of web service being used.
     *
     * @throws IllegalArgumentException if sessionMgr, webServiceType or serviceClass are null.
     */
    public <P> SalesforceWebServicePortInvoker(final WebService<P> service, final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType) {
        this.sessionMgr = IntegrityUtil.ensure(sessionMgr, "Cannot have a null session mgr!");
        this.webServiceType = IntegrityUtil.ensure(webServiceType, "Cannot have a null web service type!");
        this.port = new AtomicReference(SalesforceWebServiceUtil.createPort(sessionMgr.getSession(), webServiceType, service));
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
//            lock(proxy);
            try {
                session = getSessionMgr().getSession();

                final Object retVal = method.invoke(port.get(), args);
//                unlock(proxy);

                return retVal;
            } catch (final Exception callFailure) {
                handleException(callFailure, webService, proxy, method, session, totalCalls);
            }
        }

        throw new IllegalStateException("Should have returned a value!");
    }
}
