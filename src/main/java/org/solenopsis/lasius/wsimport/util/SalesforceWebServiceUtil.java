/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.solenopsis.lasius.wsimport.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import org.flossware.util.wsimport.UrlUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.websvc.WebSvc;

/**
 *
 * The purpose of this class is
 *
 * @author sfloess
 *
 */
public final class SalesforceWebServiceUtil {
    /**
     * Denotes an invalid session id.
     */
    public static final String INVALID_SESSION_ID = "INVALID_SESSION_ID";

    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(SalesforceWebServiceUtil.class.getName());

    /**
     * Return the logger.
     */
    protected static Logger getLogger() {
        return logger;
    }

    /**
     * Create the handler chain.
     */
    protected static void addHandlerChain(final Object bindingProvider, final SessionIdInjectHandler handler) {
        final List<Handler> handlerChain = new ArrayList<Handler>();

        handlerChain.add(handler);

        ((BindingProvider) bindingProvider).getBinding().setHandlerChain(handlerChain);

        if (getLogger().isLoggable(Level.INFO)) {
            getLogger().log(Level.INFO, "Seting session id to [{0}]", handler.getSessionId());
        }
    }

    /**
     * Set the session id.
     */
    public static void setSessionId(final Object bindingProvider, final Service service, final String sessionId) {
        addHandlerChain(bindingProvider, new SessionIdInjectHandler(service, sessionId));
    }

    /**
     * Set the session id.
     */
    public static void setSessionId(final Object bindingProvider, final Service service, final Session session) {
        setSessionId(bindingProvider, service, session.getSessionId());
    }

    /**
     * Set the session id.
     */
    public static void setSessionId(final Object bindingProvider, final WebSvc webSvc, final Session session) {
        setSessionId(bindingProvider, webSvc.getService(), session.getSessionId());
    }

    /**
     * Computes the actual Web Service URL from url, webServiceType and webServiceName.
     *
     * @param url is the base url.
     * @param webServiceType is the type of web service (enterprise, partner, metadata or custom).
     * @param webServiceName is the name of the web service to be called.
     *
     * @return a URL representation.
     */
    public static String computeUrl(final String url, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        return url + '/' + webServiceType.getUrlSuffix() + '/' + webServiceName;
    }

    /**
     * Computes the actual Web Service URL from credentials, webServiceType and webServiceName.
     *
     * @param credentials the credentials being used.
     * @param webServiceType is the type of web service (enterprise, partner, metadata or custom).
     * @param webServiceName is the name of the web service to be called.
     *
     * @return a URL representation.
     */
    public static String computeUrl(final Credentials credentials, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        return computeUrl(credentials.getUrl(), webServiceType, webServiceName);
    }

    /**
     * Computes the actual Web Service URL from session, webServiceType and webServiceName.
     *
     * @param session the session being used.
     * @param webServiceType is the type of web service (enterprise, partner, metadata or custom).
     * @param webServiceName is the name of the web service to be called.
     *
     * @return a URL representation.
     */
    public static String computeUrl(final Session session, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        return computeUrl(session.getServerUrl(), webServiceType, webServiceName);
    }

    /**
     * Set the URL on port.
     *
     * @param port the port to affect the url.
     * @param url the host of the web service.
     * @param webServiceType the type of web service.
     * @param webServiceName the web service name.
     */
    public static void setUrl(final Object port, final String url, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        UrlUtil.setUrl(port, computeUrl(url, webServiceType, webServiceName));
    }

    /**
     * Set the URL on port.
     *
     * @param port the port to affect the url.
     * @param credentials credentials being used..
     * @param webServiceType the type of web service.
     * @param webServiceName the web service name.
     */
    public static void setUrl(final Object port, final Credentials credentials, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        UrlUtil.setUrl(port, computeUrl(credentials, webServiceType, webServiceName));
    }

    /**
     * Set the URL on port.
     *
     * @param port the port to affect the url.
     * @param session the session being used.
     * @param webServiceType the type of web service.
     * @param webServiceName the web service name.
     */
    public static void setUrl(final Object port, final Session session, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        UrlUtil.setUrl(port, computeUrl(session, webServiceType, webServiceName));
    }

    /**
     * Return true if message contains invalid session id.
     *
     * @param message is the message to examine for being an invalid session id.
     */
    public static boolean isInvalidSessionId(final String message) {
        return (null == message ? false : message.contains(INVALID_SESSION_ID));
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    public static boolean isInvalidSessionId(final Exception failure) {
        if (failure instanceof InvocationTargetException) {
            return isInvalidSessionId(((InvocationTargetException) failure).getTargetException().getMessage());
        }

        return isInvalidSessionId(failure.getMessage());
    }
}
