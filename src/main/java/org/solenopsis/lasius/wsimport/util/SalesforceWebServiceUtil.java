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
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import org.flossware.util.wsimport.UrlUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.sforce.wsimport.metadata.MetadataPortType;
import org.solenopsis.lasius.sforce.wsimport.metadata.MetadataService;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.port.SalesforcePortDecorator;
import org.solenopsis.lasius.wsimport.port.impl.DefaultSalesforcePortDecorator;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * The purpose of this class is
 *
 * @author sfloess
 *
 */
public final class SalesforceWebServiceUtil {
    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(SalesforceWebServiceUtil.class.getName());

    /**
     * Manages port decoration.
     */
    private static final SalesforcePortDecorator PORT_DECORATOR = new DefaultSalesforcePortDecorator();

    /**
     * Root wsdl resource location for enterprise, metadata and partner wsdls.
     */
    public static final String ROOT_WSDL_RESOURCE = "/wsdl";

    /**
     * The enterprise wsdl resource.
     */
    public static final String ENTERPRISE_WSDL_RESOURCE = ROOT_WSDL_RESOURCE + "/enterprise.wsdl";

    /**
     * The metadata wsdl resoource.
     */
    public static final String METADATA_WSDL_RESOURCE = ROOT_WSDL_RESOURCE + "/metadata.wsdl";

    /**
     * The partner wsdl resource.
     */
    public static final String PARTNER_WSDL_RESOURCE = ROOT_WSDL_RESOURCE + "/partner.wsdl";

    /**
     * The QName for the enterprise wsdl service.
     */
    public static final QName ENTERPRISE_SERVICE_NAME = new org.solenopsis.lasius.sforce.wsimport.enterprise.SforceService().getServiceName();

    /**
     * The QName for the metadata wsdl service.
     */
    public static final QName METADATA_SERVICE_NAME = new MetadataService().getServiceName();

    /**
     * The QName for the partner wsdl service.
     */
    public static final QName PARTNER_SERVICE_NAME = new org.solenopsis.lasius.sforce.wsimport.partner.SforceService().getServiceName();

    /**
     * The actual enterprise wsdl service.
     */
    public static final org.solenopsis.lasius.sforce.wsimport.enterprise.SforceService ENTERPRISE_SERVICE =
        new org.solenopsis.lasius.sforce.wsimport.enterprise.SforceService(SalesforceWebServiceUtil.class.getResource(ENTERPRISE_WSDL_RESOURCE), ENTERPRISE_SERVICE_NAME);

    /**
     * The actual metadata wsdl service.
     */
    public static final MetadataService METADATA_SERVICE = new MetadataService(SalesforceWebServiceUtil.class.getResource(METADATA_WSDL_RESOURCE), METADATA_SERVICE_NAME);

    /**
     * The actual partner wsdl service.
     */
    public static final org.solenopsis.lasius.sforce.wsimport.partner.SforceService PARTNER_SERVICE =
        new org.solenopsis.lasius.sforce.wsimport.partner.SforceService(SalesforceWebServiceUtil.class.getResource(PARTNER_WSDL_RESOURCE), PARTNER_SERVICE_NAME);

    /**
     * The actual enterprise wsdl service's port type.
     */
    public static final Class<org.solenopsis.lasius.sforce.wsimport.enterprise.Soap> ENTERPRISE_PORT_TYPE = org.solenopsis.lasius.sforce.wsimport.enterprise.Soap.class;

    /**
     * The actual metadata wsdl service's port type.
     */
    public static final Class<MetadataPortType> METADATA_PORT_TYPE = MetadataPortType.class;

    /**
     * The actual partner wsdl service's port type.
     */
    public static final Class<org.solenopsis.lasius.sforce.wsimport.partner.Soap> PARTNER_PORT_TYPE = org.solenopsis.lasius.sforce.wsimport.partner.Soap.class;

    /**
     * Denotes an invalid session id.
     */
    public static final String INVALID_SESSION_ID = "INVALID_SESSION_ID";

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
     * Create the metadata port ready for use.
     *
     * @param session the session to use for the port.
     *
     * @return a ready to use metadata port that can reach out to Salesforce.
     *
     * @throws Exception if any problems arise creating the metadata port.
     */
    public static MetadataPortType createMetadataPort(final Session session) throws Exception {
        final MetadataPortType retVal = METADATA_SERVICE.getMetadata();

        setUrl(retVal, session, WebServiceTypeEnum.METADATA_SERVICE, session.getCredentials().getApiVersion());
        setSessionId(retVal, METADATA_SERVICE, session);

        return retVal;
    }

    /**
     * Create a ready to use metadata port.  It's decorated to handle relogins, etc.
     *
     * @param sessionMgr the session manager to use when making calls.
     *
     * @return a ready to use metdata port.
     *
     * @throws Exception if any problems arise creating the metadata port.
     */
    public static MetadataPortType createMetadataPort(final SessionMgr sessionMgr) throws Exception {
        return PORT_DECORATOR.createPort(WebServiceTypeEnum.METADATA_SERVICE, METADATA_SERVICE, METADATA_PORT_TYPE, sessionMgr.getSession().getCredentials().getApiVersion(), sessionMgr);
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
