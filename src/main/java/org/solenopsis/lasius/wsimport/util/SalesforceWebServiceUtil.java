package org.solenopsis.lasius.wsimport.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import org.flossware.util.UrlUtil;
import org.flossware.util.wsimport.port.factory.PortFactory;
import org.flossware.util.wsimport.port.factory.impl.DefaultPortFactory;
import org.flossware.util.wsimport.soap.impl.SingleAttributeSoapHeaderHandler;
import org.flossware.util.wsimport.soap.impl.SoapRequestHandler;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.sforce.wsimport.metadata.MetadataPortType;
import org.solenopsis.lasius.sforce.wsimport.metadata.MetadataService;
import org.solenopsis.lasius.sforce.wsimport.tooling.SforceServicePortType;
import org.solenopsis.lasius.sforce.wsimport.tooling.SforceServiceService;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.call.SalesforceRetryCaller;
import org.solenopsis.lasius.wsimport.security.SecurityMgr;
import org.solenopsis.lasius.wsimport.security.impl.EnterpriseSecurityMgr;
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
    private static final PortFactory DEFAULT_PORT_DECORATOR = new DefaultPortFactory();

    /**
     * Root wsdl resource location for enterprise, metadata and partner wsdls.
     */
    public static final String ROOT_WSDL_RESOURCE = "/wsdl";

    /**
     * The enterprise wsdl resource.
     */
    public static final String ENTERPRISE_WSDL_RESOURCE = ROOT_WSDL_RESOURCE + "/enterprise.wsdl";

    /**
     * The metadata wsdl resource.
     */
    public static final String METADATA_WSDL_RESOURCE = ROOT_WSDL_RESOURCE + "/metadata.wsdl";

    /**
     * The partner wsdl resource.
     */
    public static final String PARTNER_WSDL_RESOURCE = ROOT_WSDL_RESOURCE + "/partner.wsdl";

    /**
     * The tooling wsdl resource.
     */
    public static final String TOOLING_WSDL_RESOURCE = ROOT_WSDL_RESOURCE + "/tooling.wsdl";

    /**
     * The QName for the enterprise wsdl service.
     */
    public static final QName ENTERPRISE_SERVICE_NAME = new QName("urn:enterprise.soap.sforce.com", "SforceService");

    /**
     * The QName for the metadata wsdl service.
     */
    public static final QName METADATA_SERVICE_NAME = new QName("http://soap.sforce.com/2006/04/metadata", "MetadataService");

    /**
     * The QName for the partner wsdl service.
     */
    public static final QName PARTNER_SERVICE_NAME = new QName("urn:partner.soap.sforce.com", "SforceService");

    /**
     * The QName for the tooling wsdl service.
     */
    public static final QName TOOLING_SERVICE_NAME = new QName("urn:tooling.soap.sforce.com", "SforceServiceService");

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
     * The actual tooling wsdl service.
     */
    public static final SforceServiceService TOOLING_SERVICE = new SforceServiceService(SalesforceWebServiceUtil.class.getResource(TOOLING_WSDL_RESOURCE), TOOLING_SERVICE_NAME);

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
     * The actual toolings wsdl service's port type.
     */
    public static final Class<SforceServicePortType> TOOLING_PORT_TYPE = SforceServicePortType.class;

    /**
     * A security manager to use.
     */
    public static final SecurityMgr SECURITY_MANAGER = new EnterpriseSecurityMgr();

    /**
     * Denotes an invalid session id.
     */
    public static final String INVALID_SESSION_ID = "INVALID_SESSION_ID";

    /**
     * Denotes server is unavailable.
     */
    public static final String SERVER_UNAVAILABLE = "SERVER_UNAVAILABLE";

    /**
     * Denotes maximum number of retries.
     */
    public static final int MAX_RETRIES = 4;

    /**
     * Return the logger.
     */
    protected static Logger getLogger() {
        return logger;
    }

    /**
     * Return the default port decorator.
     *
     * @return the default port decorator.
     */
    protected static PortFactory<Session> getDefaultPortDecorator() {
        return DEFAULT_PORT_DECORATOR;
    }

    /**
     * Create the handler chain.
     */
    protected static void setHandlerChain(final Object bindingProvider, final SOAPHandler handler) {
        final List<Handler> handlerChain = new ArrayList<Handler>();

        handlerChain.add(handler);

        ((BindingProvider) bindingProvider).getBinding().setHandlerChain(handlerChain);

        if (getLogger().isLoggable(Level.INFO)) {
            //getLogger().log(Level.INFO, "Seting session id to [{0}]", handler.getSessionId());
        }
    }

    /**
     * Set the session id.
     */
    public static void setSessionId(final Object bindingProvider, final Service service, final String sessionId) {
        setHandlerChain(bindingProvider, new SoapRequestHandler(new SingleAttributeSoapHeaderHandler(service, "SessionHeader", "sessionId", sessionId)));  //new SessionIdInjectHandler(service, sessionId));
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
     * Prepare a port for use.
     *
     * @param service the web service being used.
     * @param port the port on the web service.
     * @param webServiceName is the name of the web service being called.
     * @param webServiceType the type of web service being used.
     * @param session the session being used.
     */
    public static void preparePort(final Service service, final Object port, final String webServiceName, final WebServiceTypeEnum webServiceType, final Session session) {
        setUrl(port, session, webServiceType, webServiceName);
        setSessionId(port, service, session);
    }


    /**
     * Return true if message contains invalid session id.
     *
     *
     * @param toCompare is the
     * @param message is the message to examine for being an invalid session id.
     */
    public static boolean isExceptionMsg(final String toCompare, final String message) {
        return (null == message ? false : message.contains(toCompare));
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    public static boolean isExceptionMsg(final String toCompare, final Throwable failure) {
        if (failure instanceof InvocationTargetException) {
            return isExceptionMsg(toCompare, ((InvocationTargetException) failure).getTargetException().getMessage());
        }

        return isExceptionMsg(toCompare, failure.getMessage());
    }

    /**
     * Return true if message contains invalid session id.
     *
     * @param message is the message to examine for being an invalid session id.
     */
    public static boolean isInvalidSessionId(final String message) {
        return isExceptionMsg(INVALID_SESSION_ID, message);
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    public static boolean isInvalidSessionId(final Throwable failure) {
        return isExceptionMsg(INVALID_SESSION_ID, failure);
    }

    /**
     * Return true if message contains server unavailable.
     *
     * @param message is the message to examine for being server unavailable.
     */
    public static boolean isServerUnavailable(final String message) {
        return isExceptionMsg(SERVER_UNAVAILABLE, message);
    }

    /**
     * Return true if we have server unavailable or false if not.
     *
     * @param failure is the failure to examine for server unavailable.
     */
    public static boolean isServerUnavailable(final Exception failure) {
        return isExceptionMsg(SERVER_UNAVAILABLE, failure);
    }

    /**
     * Return true if throwable or any of its root causes is an IOException.
     *
     * @param throwable to examine if being an IOException or any root causes is an IOException.
     *
     * @return if throwable or its root causes is an IOException, or false if not.
     */
    public static boolean containsIOException(final Throwable throwable) {
        if (null == throwable) {
            return false;
        }

        if (throwable instanceof IOException) {
            return true;
        }

        return containsIOException(throwable.getCause());
    }

    /**
     * Returns true if the failure represents one where relogin should occur.
     *
     * @param failure the exception to examine if relogin is necessary.
     *
     * @return true if relogin is necessary.
     */
    public static boolean isReloginException(final Throwable failure) {
        return isInvalidSessionId(failure) || containsIOException(failure);
    }

    /**
     * Generate a port for use.
     *
     * @param <P> the type of port we will decorate.
     *
     * @param webServiceType is the type of web service being decorated.
     * @param service is the web service to use.
     * @param portType the class of the port type.
     * @param name the name of the web service.
     *
     * @return a port with decorated functionality like limited concurrent access to SFDC.
     *
     * @throws Exception if any problems arise generating our return value.
     */
    public static <P> P createPort(final PortFactory portDecorator, final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name, final SessionMgr sessionMgr) throws Exception {
        return (P) portDecorator.createPort(service, portType, new SalesforceRetryCaller(sessionMgr, webServiceType, service, portType, name));
    }

    /**
     * Generate a port for use.
     *
     * @param <P> the type of port we will decorate.
     *
     * @param webServiceType is the type of web service being decorated.
     * @param service is the web service to use.
     * @param portType the class of the port type.
     * @param name the name of the web service.
     *
     * @return a port with decorated functionality like limited concurrent access to SFDC.
     *
     * @throws Exception if any problems arise generating our return value.
     */
    public static <P> P createPort(final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name, final SessionMgr sessionMgr) throws Exception {
        return createPort(getDefaultPortDecorator(), webServiceType, service, portType, name, sessionMgr);
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
    public static org.solenopsis.lasius.sforce.wsimport.enterprise.Soap createEnterprisePort(final Session session) throws Exception {
        final org.solenopsis.lasius.sforce.wsimport.enterprise.Soap retVal = ENTERPRISE_SERVICE.getSoap();

        preparePort(ENTERPRISE_SERVICE, retVal, session.getCredentials().getApiVersion(), WebServiceTypeEnum.ENTERPRISE_SERVICE, session);

        return retVal;
    }

    /**
     * Create the enterprise port ready for use.
     *
     * @param credentials the credentials to use for the port.
     *
     * @return a ready to use port that can reach out to Salesforce.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public static org.solenopsis.lasius.sforce.wsimport.enterprise.Soap createEnterprisePort(final Credentials credentials) throws Exception {
        return createEnterprisePort(SECURITY_MANAGER.login(credentials));
    }

    /**
     * Create a ready to use enterprise port.  It's decorated to handle relogins, etc.
     *
     * @param sessionMgr the session manager to use when making calls.
     *
     * @return a ready to use port.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public static org.solenopsis.lasius.sforce.wsimport.enterprise.Soap createEnterprisePort(final SessionMgr sessionMgr) throws Exception {
        return createPort(WebServiceTypeEnum.ENTERPRISE_SERVICE, ENTERPRISE_SERVICE, ENTERPRISE_PORT_TYPE, sessionMgr.getSession().getCredentials().getApiVersion(), sessionMgr);
    }

    /**
     * Create the metadata port ready for use.
     *
     * @param session the session to use for the port.
     *
     * @return a ready to use port that can reach out to Salesforce.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public static MetadataPortType createMetadataPort(final Session session) throws Exception {
        final MetadataPortType retVal = METADATA_SERVICE.getMetadata();

        preparePort(METADATA_SERVICE, retVal, session.getCredentials().getApiVersion(), WebServiceTypeEnum.METADATA_SERVICE, session);

        return retVal;
    }

    /**
     * Create the metadata port ready for use.
     *
     * @param credentials the credentials to use for the port.
     *
     * @return a ready to use port that can reach out to Salesforce.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public static MetadataPortType createMetadataPort(final Credentials credentials) throws Exception {
        final MetadataPortType retVal = METADATA_SERVICE.getMetadata();

        return createMetadataPort(SECURITY_MANAGER.login(credentials));
    }

    /**
     * Create a ready to use metadata port.  It's decorated to handle relogins, etc.
     *
     * @param sessionMgr the session manager to use when making calls.
     *
     * @return a ready to use port.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public static MetadataPortType createMetadataPort(final SessionMgr sessionMgr) throws Exception {
        return createPort(WebServiceTypeEnum.METADATA_SERVICE, METADATA_SERVICE, METADATA_PORT_TYPE, sessionMgr.getSession().getCredentials().getApiVersion(), sessionMgr);
    }

    /**
     * Create the partner port ready for use.
     *
     * @param session the session to use for the port.
     *
     * @return a ready to use metadata port that can reach out to Salesforce.
     *
     * @throws Exception if any problems arise creating the metadata port.
     */
    public static org.solenopsis.lasius.sforce.wsimport.partner.Soap createPartnerPort(final Session session) throws Exception {
        final org.solenopsis.lasius.sforce.wsimport.partner.Soap retVal = PARTNER_SERVICE.getSoap();

        preparePort(PARTNER_SERVICE, retVal, session.getCredentials().getApiVersion(), WebServiceTypeEnum.PARTNER_SERVICE, session);

        return retVal;
    }

    /**
     * Create the partner port ready for use.
     *
     * @param credentials the credentials to use for the port.
     *
     * @return a ready to use port that can reach out to Salesforce.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public static org.solenopsis.lasius.sforce.wsimport.partner.Soap createPartnerPort(final Credentials credentials) throws Exception {
        return createPartnerPort(SECURITY_MANAGER.login(credentials));
    }

    /**
     * Create a ready to use partner port.  It's decorated to handle relogins, etc.
     *
     * @param sessionMgr the session manager to use when making calls.
     *
     * @return a ready to use port.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public static org.solenopsis.lasius.sforce.wsimport.partner.Soap createPartnerPort(final SessionMgr sessionMgr) throws Exception {
        return createPort(WebServiceTypeEnum.PARTNER_SERVICE, PARTNER_SERVICE, PARTNER_PORT_TYPE, sessionMgr.getSession().getCredentials().getApiVersion(), sessionMgr);
    }

    /**
     * Create the tooling port ready for use.
     *
     * @param session the session to use for the port.
     *
     * @return a ready to use port that can reach out to Salesforce.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public static SforceServicePortType createToolingPort(final Session session) throws Exception {
        final SforceServicePortType retVal = TOOLING_SERVICE.getSforceService();

        preparePort(TOOLING_SERVICE, retVal, session.getCredentials().getApiVersion(), WebServiceTypeEnum.TOOLING_SERVICE, session);

        return retVal;
    }

    /**
     * Create the tooling port ready for use.
     *
     * @param credentials the credentials to use for the port.
     *
     * @return a ready to use port that can reach out to Salesforce.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public static SforceServicePortType createToolingPort(final Credentials credentials) throws Exception {
        final SforceServicePortType retVal = TOOLING_SERVICE.getSforceService();

        return createToolingPort(SECURITY_MANAGER.login(credentials));
    }

    /**
     * Create a ready to use tooling port.  It's decorated to handle relogins, etc.
     *
     * @param sessionMgr the session manager to use when making calls.
     *
     * @return a ready to use port.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public static SforceServicePortType createToolingPort(final SessionMgr sessionMgr) throws Exception {
        return createPort(WebServiceTypeEnum.TOOLING_SERVICE, TOOLING_SERVICE, TOOLING_PORT_TYPE, sessionMgr.getSession().getCredentials().getApiVersion(), sessionMgr);
    }
}
