package org.solenopsis.lasius.wsimport.common.util;

import java.util.logging.Logger;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.flossware.util.StringUtil;
import org.flossware.wsimport.service.GenericWebService;
import org.flossware.wsimport.service.WebService;
import org.flossware.wsimport.service.decorator.WebServiceInvocationDecorator;
import org.flossware.wsimport.soap.SoapUtil;
import org.flossware.wsimport.soap.header.SingleAttributeSoapHeaderHandler;
import org.flossware.wsimport.soap.header.SoapHeaderHandler;
import org.flossware.wsimport.soap.header.SoapRequestHandler;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.common.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.common.security.LoginResult;
import org.solenopsis.lasius.wsimport.common.session.Session;
import org.solenopsis.lasius.wsimport.common.session.mgr.SessionMgr;

/**
 *
 * The purpose of this class is
 *
 * @author sfloess
 *
 */
public final class SalesforceWebServiceUtil {

    /**
     * Default WSDL resource directory.
     */
    public static final String DEFAULT_WSDL_DIR = "wsdl/";

    /**
     * Our enterprise web service's WSDL.
     */
    public static final String ENTERPRISE_WEB_SERVICE_WSDL_URL = DEFAULT_WSDL_DIR + "Lasius-enterprise.wsdl";

    /**
     * Our unproxied enterprise web service.
     */
    public static final WebService<org.solenopsis.lasius.wsdls.enterprise.Soap> ENTERPRISE_WEB_SERVICE = new GenericWebService(ENTERPRISE_WEB_SERVICE_WSDL_URL, org.solenopsis.lasius.wsdls.enterprise.SforceService.class);

    /**
     * Our metadata web service's WSDL.
     */
    public static final String METADATA_WEB_SERVICE_WSDL_URL = DEFAULT_WSDL_DIR + "Lasius-metadata.wsdl";

    /**
     * Our unproxied metadata web service.
     */
    public static final WebService<org.solenopsis.lasius.wsdls.metadata.MetadataPortType> METADATA_WEB_SERVICE = new GenericWebService(METADATA_WEB_SERVICE_WSDL_URL, org.solenopsis.lasius.wsdls.metadata.MetadataService.class);

    /**
     * Our partner web service's WSDL.
     */
    public static final String PARTNER_WEB_SERVICE_WSDL_URL = DEFAULT_WSDL_DIR + "Lasius-partner.wsdl";

    /**
     * Our unproxied partner web service.
     */
    public static final WebService<org.solenopsis.lasius.wsdls.partner.Soap> PARTNER_WEB_SERVICE = new GenericWebService(PARTNER_WEB_SERVICE_WSDL_URL, org.solenopsis.lasius.wsdls.partner.SforceService.class);

    /**
     * Our tooling web service's WSDL.
     */
    public static final String TOOLING_WEB_SERVICE_WSDL_URL = DEFAULT_WSDL_DIR + "Lasius-tooling.wsdl";

    /**
     * Our unproxied tooling web service.
     */
    public static final WebService<org.solenopsis.lasius.wsdls.tooling.SforceServicePortType> TOOLING_WEB_SERVICE = new GenericWebService(TOOLING_WEB_SERVICE_WSDL_URL, org.solenopsis.lasius.wsdls.tooling.SforceServiceService.class);

    /**
     * Denotes an invalid session id.
     */
    public static final String INVALID_SESSION_ID = "INVALID_SESSION_ID";

    /**
     * Denotes server is unavailable.
     */
    public static final String SERVER_UNAVAILABLE = "SERVER_UNAVAILABLE";

    /**
     * When setting up the soap header, we need to se the session header using
     * this attribute.
     */
    public static final String SESSION_HEADER = "SessionHeader";

    /**
     * This is the session id on the session header.
     */
    public static final String SESSION_ID = "sessionId";

    /**
     * Separator for urls.
     */
    public static final String URL_SEPARATOR = "/";

    /**
     * Denotes maximum number of retries.
     */
    public static final int MAX_RETRIES = 4;

    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(SalesforceWebServiceUtil.class.getName());

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected static Logger getLogger() {
        return logger;
    }

    /**
     * Computes the actual Web Service URL from url, webServiceType and
     * webServiceName.
     *
     * @param url            is the base url.
     * @param webServiceType is the type of web service (enterprise, partner,
     *                       metadata or custom).
     * @param webServiceName is the name of the web service to be called.
     *
     * @return a URL representation.
     */
    public static String computeUrl(final String url, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        return StringUtil.concatWithSeparator(URL_SEPARATOR, url, webServiceType.getUrlSuffix(), webServiceName);
    }

    /**
     * Computes the actual Web Service URL from session, webServiceType and
     * webServiceName.
     *
     * @param loginResult    result of a login.
     * @param webServiceType is the type of web service (enterprise, partner,
     *                       metadata or custom).
     * @param webServiceName is the name of the web service to be called.
     *
     * @return a URL representation.
     */
    public static String computeUrl(final LoginResult loginResult, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        return computeUrl(loginResult.getServerUrl(), webServiceType, webServiceName);
    }

    /**
     * Computes the actual Web Service URL from session, webServiceType and
     * webServiceName.
     *
     * @param session        the session being used.
     * @param webServiceType is the type of web service (enterprise, partner,
     *                       metadata or custom).
     * @param webServiceName is the name of the web service to be called.
     *
     * @return a URL representation.
     */
    public static String computeUrl(final Session session, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        return computeUrl(session.getLoginResult(), webServiceType, webServiceName);
    }

    /**
     * Computes the actual Web Service URL from credentials, webServiceType and
     * webServiceName.
     *
     * @param credentials    the credentials being used.
     * @param webServiceType is the type of web service (enterprise, partner,
     *                       metadata or custom).
     * @param webServiceName is the name of the web service to be called.
     *
     * @return a URL representation.
     */
    public static String computeUrl(final Credentials credentials, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        return computeUrl(credentials.getUrl(), webServiceType, webServiceName);
    }

    /**
     * Create a soap handler header for the session id.
     *
     * @param service   the web service.
     * @param sessionId session id to use on the soap header.
     *
     * @return a usable soap header handler.
     */
    public static SoapHeaderHandler createSessionIdSoapHandlerHeader(final WebService service, final String sessionId) {
        return new SingleAttributeSoapHeaderHandler(service.getQName(), SESSION_HEADER, SESSION_ID, sessionId);
    }

    /**
     * Create a soap request handler to honor session ids.
     *
     * @param service   the web service.
     * @param sessionId session id to use on the soap header.
     *
     * @return
     */
    public static SOAPHandler<SOAPMessageContext> createSessionIdSoapHandler(final WebService service, final String sessionId) {
        return new SoapRequestHandler(createSessionIdSoapHandlerHeader(service, sessionId));
    }

    /**
     * Set the session id on the binding provider.
     *
     * @param bindingProvider the object who gets the session id.
     * @param service         the web service.
     * @param sessionId       the session id to be used.
     */
    public static void setSessionId(final Object bindingProvider, final WebService service, final String sessionId) {
        SoapUtil.setHandlerChain(bindingProvider, createSessionIdSoapHandler(service, sessionId));
    }

    /**
     * Set the session id on the binding provider.
     *
     * @param bindingProvider the object who gets the session id.
     * @param service         the web service.
     * @param loginResult     the login result that contains the session id.
     */
    public static void setSessionId(final Object bindingProvider, final WebService service, final LoginResult loginResult) {
        setSessionId(bindingProvider, service, loginResult.getSessionId());
    }

    /**
     * Set the session id on the binding provider.
     *
     * @param bindingProvider the object who gets the session id.
     * @param service         the web service.
     * @param session         the session containing the session id.
     */
    public static void setSessionId(final Object bindingProvider, final WebService service, final Session session) {
        setSessionId(bindingProvider, service, session.getLoginResult());
    }

    /**
     * Set the URL on port.
     *
     * @param port           the port to affect the url.
     * @param url            the host of the web service.
     * @param webServiceType the type of web service.
     * @param webServiceName the web service name.
     */
    public static <P> void setUrl(final P port, final String url, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        SoapUtil.setUrl(port, computeUrl(url, webServiceType, webServiceName));
    }

    /**
     * Set the URL on port.
     *
     * @param port           the port to affect the url.
     * @param credentials    credentials being used..
     * @param webServiceType the type of web service.
     * @param webServiceName the web service name.
     */
    public static void setUrl(final Object port, final Credentials credentials, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        setUrl(port, credentials.getUrl(), webServiceType, webServiceName);
    }

    /**
     * Set the URL on port.
     *
     * @param port           the port to affect the url.
     * @param loginResult    the result of login.
     * @param webServiceType the type of web service.
     * @param webServiceName the web service name.
     */
    public static void setUrl(final Object port, final LoginResult loginResult, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        setUrl(port, loginResult.getServerUrl(), webServiceType, webServiceName);
    }

    /**
     * Set the URL on port.
     *
     * @param port           the port to affect the url.
     * @param session        the session being used.
     * @param webServiceType the type of web service.
     * @param webServiceName the web service name.
     */
    public static void setUrl(final Object port, final Session session, final WebServiceTypeEnum webServiceType, final String webServiceName) {
        setUrl(port, session.getLoginResult(), webServiceType, webServiceName);
    }

    /**
     * Return true if webServiceType is a custom web service or false if not.
     *
     * @param webServiceType the enum denoting the web service type.
     *
     * @return true if webServiceType is a custom web service or false if not.
     */
    public static boolean isCustomService(final WebServiceTypeEnum webServiceType) {
        return WebServiceTypeEnum.CUSTOM_SERVICE == webServiceType;
    }

    /**
     * Return the web service name. If its a custom web service, we can glean
     * the web service name from the serviceClass. Otherwise, its one of the
     * SFDC web services - in which case the web service name is the API version
     * as contained in the credentials.
     *
     * @param credentials    contains the API version.
     * @param webServiceType the type of web service.
     * @param service        the web service.
     *
     * @return the web service name. For custom web services, it's found in the
     *         service class, otherwise its the API version in the credentials.
     */
    public static String computeWebServiceName(final Credentials credentials, final WebServiceTypeEnum webServiceType, final WebService service) {
        return isCustomService(webServiceType) ? service.getPortName() : credentials.getApiVersion();
    }

    /**
     * Return the web service name. If its a custom web service, we can glean
     * the web service name from the serviceClass. Otherwise, its one of the
     * SFDC web services - in which case the web service name is the API version
     * as contained in the loginResult's credentials.
     *
     * @param loginResult    contains the credentials who contain the API
     *                       version.
     * @param webServiceType the type of web service.
     * @param service        the web service.
     *
     * @return the web service name. For custom web services, it's found in the
     *         service class, otherwise its the API version in the credentials
     *         within the loginResult.
     */
    public static String computeWebServiceName(final LoginResult loginResult, final WebServiceTypeEnum webServiceType, final WebService service) {
        return SalesforceWebServiceUtil.computeWebServiceName(loginResult.getCredentials(), webServiceType, service);
    }

    /**
     * Creates a port.
     *
     * @param <P>            the type of web service endpoint.
     * @param url            the url to use when talking to SFDC.
     * @param webServiceType the type of web service.
     * @param service        the web service.
     * @param serviceName    the name of the web service - for example for
     *                       custom web services, its contained in the service
     *                       class. For stock SFDC services like metadata,
     *                       tooling, partner or enterprise - its the API
     *                       version.
     *
     * @return a usable port.
     */
    public static <P> P createPort(final String url, final WebServiceTypeEnum webServiceType, final WebService<P> service, final String serviceName) {
        final P retVal = service.getPort();

        setUrl(retVal, url, webServiceType, serviceName);

        return retVal;
    }

    /**
     * Create a port who has a session id.
     *
     * @param <P>            the type of web service endpoint.
     * @param sessionId      the session id to use for SFDC.
     * @param url            the url to use when talking to SFDC.
     * @param webServiceType the type of web service.
     * @param service        the web service.
     * @param serviceName    the name of the web service - for example for
     *                       custom web services, its contained in the service
     *                       class. For stock SFDC services like metadata,
     *                       tooling, partner or enterprise - its the API
     *                       version.
     *
     * @return a usable port.
     */
    public static <P> P createPort(final String sessionId, final String url, final WebServiceTypeEnum webServiceType, final WebService<P> service, final String serviceName) {
        final P retVal = createPort(url, webServiceType, service, serviceName);

        setSessionId(retVal, service, sessionId);

        return retVal;
    }

    /**
     * Create a port using data from the loginResult.
     *
     * @param <P>            the type of web service endpoint.
     * @param loginResult    contains the session id to use for SFDC.
     * @param webServiceType the type of web service.
     * @param service        the web service.
     *
     * @return a usable port.
     */
    public static <P> P createPort(final LoginResult loginResult, final WebServiceTypeEnum webServiceType, final WebService<P> service) {
        return createPort(loginResult.getSessionId(), loginResult.getServerUrl(), webServiceType, service, computeWebServiceName(loginResult, webServiceType, service));
    }

    /**
     * Create a port using data from the session.
     *
     * @param <P>            the type of web service endpoint.
     * @param session        contains a loginResult which in turns contains a
     *                       session id to use for SFDC.
     * @param webServiceType the type of web service.
     * @param service        the web service.
     *
     * @return a usable port.
     */
    public static <P> P createPort(final Session session, final WebServiceTypeEnum webServiceType, final WebService<P> service) {
        return createPort(session.getLoginResult(), webServiceType, service);
    }

    /**
     * Create a port usind data from the sessionMgr.
     *
     * @param <P>            the type of web service endpoint.
     * @param sessionMgr     contains a session which contains a loginResult,
     *                       containing a session id to use for SFDC.
     * @param webServiceType the type of web service.
     * @param service        the web service.
     *
     * @return a usable port.
     */
    public static <P> P createPort(final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType, final WebService<P> service) {
        return createPort(sessionMgr.getSession(), webServiceType, service);
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>            the type of web service endpoint.
     * @param sessionMgr     will be used to create sessions for SFDC calls.
     * @param webServiceType the type of web service being used.
     * @param service        the web service.
     *
     * @return a usable port.
     */
    public static <P> P createProxyPort(final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType, final WebService<P> service) {
        return new WebServiceInvocationDecorator<>(service, new SalesforceWebServicePortInvoker(sessionMgr, webServiceType)).getPort();
    }
}
