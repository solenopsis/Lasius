package org.solenopsis.lasius.wsimport.util;

import java.net.URL;
import java.util.Collection;
import java.util.logging.Logger;
import javax.xml.ws.Service;
import org.flossware.wsimport.service.GenericWebService;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.common.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.common.security.SecurityMgr;
import org.solenopsis.lasius.wsimport.common.session.mgr.MultiSessionMgr;
import org.solenopsis.lasius.wsimport.common.session.mgr.SessionMgr;
import org.solenopsis.lasius.wsimport.common.session.mgr.SingleSessionMgr;
import org.solenopsis.lasius.wsimport.common.util.SalesforceWebServiceUtil;
import org.solenopsis.lasius.wsimport.enterprise.security.EnterpriseSecurityMgr;

/**
 *
 * Custom web service util.
 *
 * @author sfloess
 *
 */
public final class CustomWebServiceUtil {

    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(CustomWebServiceUtil.class.getName());

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected static Logger getLogger() {
        return logger;
    }

    /**
     * Default security mgr.
     */
    public static final SecurityMgr DEFAULT_SECURITY_MGR = new EnterpriseSecurityMgr();

    /**
     * Create a custom port. You will be logged in once this call returns.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param wsdlLocation the URL to the web service WSDL.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomPort(final Credentials credentials, final URL wsdlLocation, final Class<? extends Service> serviceClass) throws Exception {
        return (P) SalesforceWebServiceUtil.createPort(DEFAULT_SECURITY_MGR.login(credentials), WebServiceTypeEnum.CUSTOM_SERVICE, new GenericWebService(wsdlLocation, serviceClass));
    }

    /**
     * Create a custom port. You will be logged in once this call returns.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param wsdlResource the classpath WSDL resource.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomPort(final Credentials credentials, final String wsdlResource, final Class<? extends Service> serviceClass) throws Exception {
        return (P) SalesforceWebServiceUtil.createPort(DEFAULT_SECURITY_MGR.login(credentials), WebServiceTypeEnum.CUSTOM_SERVICE, new GenericWebService(wsdlResource, serviceClass));
    }

    /**
     * Create a custom port. You will be logged in once this call returns.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomPort(final Credentials credentials, final Class<? extends Service> serviceClass) throws Exception {
        return (P) SalesforceWebServiceUtil.createPort(DEFAULT_SECURITY_MGR.login(credentials), WebServiceTypeEnum.CUSTOM_SERVICE, new GenericWebService(serviceClass));
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param sessionMgr   will be used to create sessions for SFDC calls.
     * @param wsdlLocation the URL to the web service WSDL.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final SessionMgr sessionMgr, final URL wsdlLocation, final Class<? extends Service> serviceClass) throws Exception {
        return (P) SalesforceWebServiceUtil.createProxyPort(sessionMgr, WebServiceTypeEnum.CUSTOM_SERVICE, new GenericWebService(wsdlLocation, serviceClass));
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param sessionMgr   will be used to create sessions for SFDC calls.
     * @param wsdlResource the classpath WSDL resource.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final SessionMgr sessionMgr, final String wsdlResource, final Class<? extends Service> serviceClass) throws Exception {
        return (P) SalesforceWebServiceUtil.createProxyPort(sessionMgr, WebServiceTypeEnum.CUSTOM_SERVICE, new GenericWebService(wsdlResource, serviceClass));
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param sessionMgr   will be used to create sessions for SFDC calls.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final SessionMgr sessionMgr, final Class<? extends Service> serviceClass) throws Exception {
        return (P) SalesforceWebServiceUtil.createProxyPort(sessionMgr, WebServiceTypeEnum.CUSTOM_SERVICE, new GenericWebService(serviceClass));
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param wsdlLocation the URL to the web service WSDL.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final Credentials[] credentials, final URL wsdlLocation, final Class<? extends Service> serviceClass) throws Exception {
        return createCustomProxyPort(new MultiSessionMgr(credentials, DEFAULT_SECURITY_MGR), wsdlLocation, serviceClass);
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param wsdlLocation the URL to the web service WSDL.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final Collection<Credentials> credentials, final URL wsdlLocation, final Class<? extends Service> serviceClass) throws Exception {
        return createCustomProxyPort(new MultiSessionMgr(credentials, DEFAULT_SECURITY_MGR), wsdlLocation, serviceClass);
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param wsdlLocation the URL to the web service WSDL.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final Credentials credentials, final URL wsdlLocation, final Class<? extends Service> serviceClass) throws Exception {
        return createCustomProxyPort(new SingleSessionMgr(credentials, DEFAULT_SECURITY_MGR), wsdlLocation, serviceClass);
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param wsdlResource the classpath WSDL resource.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final Credentials[] credentials, final String wsdlResource, final Class<? extends Service> serviceClass) throws Exception {
        return createCustomProxyPort(new MultiSessionMgr(credentials, DEFAULT_SECURITY_MGR), wsdlResource, serviceClass);
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param wsdlResource the classpath WSDL resource.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final Collection<Credentials> credentials, final String wsdlResource, final Class<? extends Service> serviceClass) throws Exception {
        return createCustomProxyPort(new MultiSessionMgr(credentials, DEFAULT_SECURITY_MGR), wsdlResource, serviceClass);
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param wsdlResource the classpath WSDL resource.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final Credentials credentials, final String wsdlResource, final Class<? extends Service> serviceClass) throws Exception {
        return createCustomProxyPort(new SingleSessionMgr(credentials, DEFAULT_SECURITY_MGR), wsdlResource, serviceClass);
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final Credentials[] credentials, final Class<? extends Service> serviceClass) throws Exception {
        return createCustomProxyPort(new MultiSessionMgr(credentials, DEFAULT_SECURITY_MGR), serviceClass);
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final Collection<Credentials> credentials, final Class<? extends Service> serviceClass) throws Exception {
        return createCustomProxyPort(new MultiSessionMgr(credentials, DEFAULT_SECURITY_MGR), serviceClass);
    }

    /**
     * Create a proxied port. This manages auto login and retries when calling
     * SFDC.
     *
     * @param <P>          the type of web service endpoint.
     * @param credentials  the credentials for login.
     * @param serviceClass the class of a custom Web Service being used.
     *
     * @return a usable port.
     *
     * @throws Exception if any problems arise creating the web service
     *                   endpoint.
     */
    public static <P> P createCustomProxyPort(final Credentials credentials, final Class<? extends Service> serviceClass) throws Exception {
        return createCustomProxyPort(new SingleSessionMgr(credentials, DEFAULT_SECURITY_MGR), serviceClass);
    }
}
