package org.solenopsis.lasius.wsimport.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import org.flossware.util.UrlUtil;
import org.flossware.util.reflect.call.method.MethodDecorator;
import org.flossware.util.reflect.call.method.impl.DefaultMethodDecorator;
import org.flossware.util.wsimport.reflect.ServiceMgr;
import org.flossware.util.wsimport.reflect.impl.CachingServiceMgr;
import org.flossware.util.wsimport.soap.impl.SingleAttributeSoapHeaderHandler;
import org.flossware.util.wsimport.soap.impl.SoapRequestHandler;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.security.SecurityMgr;
import org.solenopsis.lasius.wsimport.security.impl.EnterpriseSecurityMgr;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;
import org.solenopsis.wsdls.metadata.MetadataPortType;
import org.solenopsis.wsdls.metadata.MetadataService;
import org.solenopsis.wsdls.tooling.LoginResult;
import org.solenopsis.wsdls.tooling.SforceServicePortType;
import org.solenopsis.wsdls.tooling.SforceServiceService;

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
    public static final ServiceMgr SERVICE_MGR = new CachingServiceMgr();

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
     * Set the session id.
     */
    public static void setSessionId(final Object bindingProvider, final Class <? extends Service> serviceClass, final String sessionId) {
        setHandlerChain(bindingProvider, new SoapRequestHandler(new SingleAttributeSoapHeaderHandler(SERVICE_MGR.getServiceQName(serviceClass), "SessionHeader", "sessionId", sessionId)));  //new SessionIdInjectHandler(service, sessionId));
    }

    /**
     * Set the session id.
     */
    public static void setSessionId(final Object bindingProvider, final Class <? extends Service> serviceClass, final Session session) {
        setSessionId(bindingProvider, serviceClass, session.getSessionId());
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
		setUrl(port, credentials.getUrl(), webServiceType, webServiceName);
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
		setUrl(port, session.getServerUrl(), webServiceType, webServiceName);
    }
	

	public static org.solenopsis.wsdls.enterprise.SforceService getDefaultEnterpriseService() throws Exception {
		return SERVICE_MGR.createPort(org.solenopsis.wsdls.enterprise.SforceService.class);
	}

	public static org.solenopsis.wsdls.partner.SforceService getDefaultPartnerService() throws Exception {
		return SERVICE_MGR.createPort(org.solenopsis.wsdls.partner.SforceService.class);
	}

	public static SforceServiceService getDefaultToolingService() throws Exception {
		return SERVICE_MGR.createPort(SforceServiceService.class);
	}
	    
    
    public static org.solenopsis.wsdls.enterprise.LoginResult enterpriseLogin(final Credentials credentials) throws Exception {
		return createEnterprisePort(credentials.getUrl(), credentials.getApiVersion()).login(credentials.getUserName(), credentials.getSecurityPassword());
    }
   
    public static org.solenopsis.wsdls.partner.LoginResult partnerLogin(final Credentials credentials) throws Exception {
        return  createPartnerPort(credentials.getUrl(), credentials.getApiVersion()).login(credentials.getUserName(), credentials.getSecurityPassword());
    }
      
    public static LoginResult toolingLogin(final Credentials credentials) throws Exception {
        return  createToolingPort(credentials.getUrl(), credentials.getApiVersion()).login(credentials.getUserName(), credentials.getSecurityPassword());
    }
    
	
    public static void enterpriseLogout(final Session session) throws Exception {
        createEnterprisePort(session).logout();
    }
    
    public static void partnerLogout(final Session session) throws Exception {
        createPartnerPort(session).logout();
    }
        
    public static void toolingLogout(final Session session) throws Exception {
        createToolingPort(session).logout();
    }  
	
	
	
    public static <P> P createPort(final String url, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final String webServiceName) throws Exception {
        final P retVal = SERVICE_MGR.createPort(serviceClass);

        setUrl(retVal, url, webServiceType, webServiceName);

        return retVal;
    }

	public static <P> P createEnterprisePort(final String url, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(url, WebServiceTypeEnum.ENTERPRISE_SERVICE, serviceClass, apiVersion);
    }
    
    public static <P> P createPartnerPort(final String url, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(url, WebServiceTypeEnum.PARTNER_SERVICE, serviceClass, apiVersion);
    }
    
    public static <P> P createToolingPort(final String url, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(url, WebServiceTypeEnum.TOOLING_SERVICE, serviceClass, apiVersion);
    }

	public static org.solenopsis.wsdls.enterprise.Soap createEnterprisePort(final String url, final String apiVersion) throws Exception {
        return createEnterprisePort(url, org.solenopsis.wsdls.enterprise.SforceService.class, apiVersion);
    }
    
    public static org.solenopsis.wsdls.partner.Soap createPartnerPort(final String url, final String apiVersion) throws Exception {
        return createPort(url, WebServiceTypeEnum.PARTNER_SERVICE, org.solenopsis.wsdls.partner.SforceService.class, apiVersion);
    }
    
    public static SforceServicePortType createToolingPort(final String url, final String apiVersion) throws Exception {
        return createToolingPort(url, SforceServiceService.class, apiVersion);
    }
	
	
	
	
	
	public static <P> P createPort(final String sessionId, final String url, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final String webServiceName) throws Exception {
        final P retVal = createPort(url, webServiceType, serviceClass, webServiceName);

        setSessionId(retVal, serviceClass, sessionId);

        return retVal;
    }
	
	public static <P> P createPort(final org.solenopsis.wsdls.enterprise.LoginResult loginResult, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final String webServiceName) throws Exception {
        return createPort(loginResult.getSessionId(), loginResult.getServerUrl(), webServiceType, serviceClass, webServiceName);
    }
	
	public static <P> P createPort(final org.solenopsis.wsdls.partner.LoginResult loginResult, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final String webServiceName) throws Exception {
        return createPort(loginResult.getSessionId(), loginResult.getServerUrl(), webServiceType, serviceClass, webServiceName);
    }
	
	public static <P> P createPort(final LoginResult loginResult, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final String webServiceName) throws Exception {
        return createPort(loginResult.getSessionId(), loginResult.getServerUrl(), webServiceType, serviceClass, webServiceName);
    }
	
    public static <P> P createPort(final Credentials credentials, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final String webServiceName) throws Exception {
		return createPort(enterpriseLogin(credentials), webServiceType, serviceClass, webServiceName);
    }
	
    public static <P> P createPort(final Session session, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final String webServiceName) throws Exception {
        return createPort(session.getSessionId(), session.getServerUrl(), webServiceType, serviceClass, webServiceName);
    }
    
    public static <P> P createPort(final Session session, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass) throws Exception {
        return createPort(session, webServiceType, serviceClass, session.getCredentials().getApiVersion());
    }
	
	
	
    
    public static <P> P createEnterprisePort(final Credentials credentials, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(enterpriseLogin(credentials), WebServiceTypeEnum.ENTERPRISE_SERVICE, serviceClass, apiVersion);
    }
       
    public static <P> P createMetadataPort(final Credentials credentials, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(enterpriseLogin(credentials), WebServiceTypeEnum.METADATA_SERVICE, serviceClass, apiVersion);
    }
    
    public static <P> P createPartnerPort(final Credentials credentials, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(partnerLogin(credentials), WebServiceTypeEnum.PARTNER_SERVICE, serviceClass, apiVersion);
    }
    
    public static <P> P createToolingPort(final Credentials credentials, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(toolingLogin(credentials), WebServiceTypeEnum.TOOLING_SERVICE, serviceClass, apiVersion);
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
    public static <P> P createEnterprisePort(final Credentials credentials, Class<? extends Service> serviceClass) throws Exception {
        return createEnterprisePort(credentials, serviceClass, credentials.getApiVersion());
    }
      
    public static <P> P createMetadataPort(final Credentials credentials, Class<? extends Service> serviceClass) throws Exception {
        return createMetadataPort(credentials, serviceClass, credentials.getApiVersion());
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
    public static <P> P createPartnerPort(final Credentials credentials, Class<? extends Service> serviceClass) throws Exception {
        return createPartnerPort(credentials, serviceClass, credentials.getApiVersion());
    }
      
    public static <P> P createToolingPort(final Credentials credentials, Class<? extends Service> serviceClass) throws Exception {
        return createToolingPort(credentials, serviceClass, credentials.getApiVersion());
    }    
    
    
    
    public static org.solenopsis.wsdls.enterprise.Soap createEnterprisePort(final Credentials credentials, final String apiVersion) throws Exception {
        return createEnterprisePort(credentials, org.solenopsis.wsdls.enterprise.SforceService.class, apiVersion);
    }

    public static MetadataPortType createMetadataPort(final Credentials credentials, final String apiVersion) throws Exception {
        return createMetadataPort(credentials, MetadataService.class, apiVersion);
    }
    
    public static org.solenopsis.wsdls.partner.Soap createPartnerPort(final Credentials credentials, final String apiVersion) throws Exception {
        return createPartnerPort(credentials, org.solenopsis.wsdls.partner.SforceService.class, apiVersion);
    }
 
    public static SforceServicePortType createToolingPort(final Credentials credentials, final String apiVersion) throws Exception {
        return createToolingPort(credentials, SforceServiceService.class, apiVersion);
    }   
    
    
    public static org.solenopsis.wsdls.enterprise.Soap createEnterprisePort(final Credentials credentials) throws Exception {
        return createEnterprisePort(credentials, org.solenopsis.wsdls.enterprise.SforceService.class);
    }

    public static MetadataPortType createMetadataPort(final Credentials credentials) throws Exception {
        return createMetadataPort(credentials, MetadataService.class);
    }
    
    public static org.solenopsis.wsdls.partner.Soap createPartnerPort(final Credentials credentials) throws Exception {
        return createPartnerPort(credentials, org.solenopsis.wsdls.partner.SforceService.class);
    }
    
    public static SforceServicePortType createToolingPort(final Credentials credentials) throws Exception {
        return createToolingPort(credentials, SforceServiceService.class);
    }  
 
    
    
    public static <P> P createEnterprisePort(final Session session, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(session, WebServiceTypeEnum.ENTERPRISE_SERVICE, serviceClass, apiVersion);
    }
     
    public static <P> P createMetadataPort(final Session session, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(session, WebServiceTypeEnum.METADATA_SERVICE, serviceClass, apiVersion);
    }
     
    public static <P> P createPartnerPort(final Session session, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(session, WebServiceTypeEnum.PARTNER_SERVICE, serviceClass, apiVersion);
    }
      
    public static <P> P createToolingPort(final Session session, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPort(session, WebServiceTypeEnum.TOOLING_SERVICE, serviceClass, apiVersion);
    }  
     
    public static <P> P createCustomPort(final Session session, Class<? extends Service> serviceClass, final String name) throws Exception {
        return createPort(session, WebServiceTypeEnum.CUSTOM_SERVICE, serviceClass, name);
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
    public static <P> P createEnterprisePort(final Session session, Class<? extends Service> serviceClass) throws Exception {
        return createEnterprisePort(session, serviceClass, session.getCredentials().getApiVersion());
    }
    
    public static <P> P createMetadataPort(final Session session, Class<? extends Service> serviceClass) throws Exception {
        return createMetadataPort(session, serviceClass, session.getCredentials().getApiVersion());
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
    public static <P> P createPartnerPort(final Session session, Class<? extends Service> serviceClass) throws Exception {
        return createPartnerPort(session, serviceClass, session.getCredentials().getApiVersion());
    }   
       
    public static <P> P createToolingPort(final Session session, Class<? extends Service> serviceClass) throws Exception {
        return createToolingPort(session, serviceClass, session.getCredentials().getApiVersion());
    } 
     
    public static <P> P createCustomPort(final Session session, Class<? extends Service> serviceClass) throws Exception {
        return createPort(session, WebServiceTypeEnum.CUSTOM_SERVICE, serviceClass, SERVICE_MGR.getPortName(serviceClass));
    }   
    
    
    
    public static org.solenopsis.wsdls.enterprise.Soap creatEnterprisePort(final Session session, final String apiVersion) throws Exception {
        return createEnterprisePort(session, org.solenopsis.wsdls.enterprise.SforceService.class, apiVersion);
    }
    
    public static MetadataPortType createMetadataPort(final Session session, final String apiVersion) throws Exception {
        return createMetadataPort(session, MetadataService.class, apiVersion);
    }   
    
    public static org.solenopsis.wsdls.partner.Soap createPartnerPort(final Session session, final String apiVersion) throws Exception {
        return createPartnerPort(session, org.solenopsis.wsdls.partner.SforceService.class, apiVersion);
    }   
    
    public static SforceServicePortType createToolingPort(final Session session, final String apiVersion) throws Exception {
        return createToolingPort(session, org.solenopsis.wsdls.tooling.SforceServiceService.class, apiVersion);
    }    
    
    
    
    public static org.solenopsis.wsdls.enterprise.Soap createEnterprisePort(final Session session) throws Exception {
        return createEnterprisePort(session, org.solenopsis.wsdls.enterprise.SforceService.class);
    }
    
    public static MetadataPortType createMetadataPort(final Session session) throws Exception {
        return createMetadataPort(session, MetadataService.class);
    }
    
    public static org.solenopsis.wsdls.partner.Soap createPartnerPort(final Session session) throws Exception {
        return createPartnerPort(session, org.solenopsis.wsdls.partner.SforceService.class);
    }
    
    public static SforceServicePortType createToolingPort(final Session session) throws Exception {
        return createToolingPort(session, org.solenopsis.wsdls.tooling.SforceServiceService.class);
    }
 

    
    public static <P> P createEnterprisePort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createEnterprisePort(sessionMgr.getSession(), serviceClass, apiVersion);
    }
    
    public static <P> P createMetadataPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createMetadataPort(sessionMgr.getSession(), serviceClass, apiVersion);
    }
    
    public static <P> P createPartnerPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createPartnerPort(sessionMgr.getSession(), serviceClass, apiVersion);
    }
          
    public static <P> P createToolingPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createToolingPort(sessionMgr.getSession(), serviceClass, apiVersion);
    }   
           
    public static <P> P createCustomPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String name) throws Exception {
        return createCustomPort(sessionMgr.getSession(), serviceClass, name);
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
    public static <P> P createEnterprisePort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass) throws Exception {
        return createEnterprisePort(sessionMgr.getSession(), serviceClass);
    }
      
    public static <P> P createMetadataPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass) throws Exception {
        return createMetadataPort(sessionMgr.getSession(), serviceClass);
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
    public static <P> P createPartnerPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass) throws Exception {
        return createPartnerPort(sessionMgr.getSession(), serviceClass);
    }   

    public static <P> P createToolingPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass) throws Exception {
        return createToolingPort(sessionMgr.getSession(), serviceClass);
    }
     
    public static <P> P createCustomPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass) throws Exception {
        return createCustomPort(sessionMgr.getSession(), serviceClass);
    }   
    
    
    
    public static org.solenopsis.wsdls.enterprise.Soap createEnterprisePort(final SessionMgr sessionMgr, final String apiVersion) throws Exception {
        return createEnterprisePort(sessionMgr, org.solenopsis.wsdls.enterprise.SforceService.class, apiVersion);
    }  
     
    public static MetadataPortType createMetadataPort(final SessionMgr sessionMgr, final String apiVersion) throws Exception {
        return createMetadataPort(sessionMgr, MetadataService.class, apiVersion);
    }
     
    public static org.solenopsis.wsdls.partner.Soap createPartnerPort(final SessionMgr sessionMgr, final String apiVersion) throws Exception {
        return createPartnerPort(sessionMgr, org.solenopsis.wsdls.partner.SforceService.class, apiVersion);
    }   
       
    public static SforceServicePortType createToolingPort(final SessionMgr sessionMgr, final String apiVersion) throws Exception {
        return createToolingPort(sessionMgr, org.solenopsis.wsdls.tooling.SforceServiceService.class, apiVersion);
    }  



    
    public static org.solenopsis.wsdls.enterprise.Soap createEnterprisePort(final SessionMgr sessionMgr) throws Exception {
        return createEnterprisePort(sessionMgr, org.solenopsis.wsdls.enterprise.SforceService.class);
    }
 
    public static MetadataPortType createMetadataPort(final SessionMgr sessionMgr) throws Exception {
        return createMetadataPort(sessionMgr, MetadataService.class);
    }
    
    public static org.solenopsis.wsdls.partner.Soap createPartnerPort(final SessionMgr sessionMgr) throws Exception {
        return createPartnerPort(sessionMgr, org.solenopsis.wsdls.partner.SforceService.class);
    }
 
    public static SforceServicePortType createToolingPort(final SessionMgr sessionMgr) throws Exception {
        return createToolingPort(sessionMgr, org.solenopsis.wsdls.tooling.SforceServiceService.class);
    }

    
    
    public static <P> P createProxyPort(final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final String name, final URL wsdlUrl) throws Exception {
        return (P) (
            Proxy.newProxyInstance(
                SalesforceWebServiceUtil.class.getClassLoader(),
                SERVICE_MGR.createPort(serviceClass, wsdlUrl).getClass().getInterfaces(),
                new PortInvoker(sessionMgr, webServiceType, serviceClass, name)
            )
        );
    }

    
    public static <P> P createProxyPort(final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final URL wsdlUrl) throws Exception {
        return createProxyPort(sessionMgr, webServiceType, serviceClass, SERVICE_MGR.getPortName(serviceClass), wsdlUrl);
    }
    
    
    public static <P> P createEnterpriseProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion, final URL wsdlUrl) throws Exception {
        return createProxyPort(sessionMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE, serviceClass, apiVersion, wsdlUrl);
    }
      
    public static <P> P createMetadataProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion, final URL wsdlUrl) throws Exception {
        return createProxyPort(sessionMgr, WebServiceTypeEnum.METADATA_SERVICE, serviceClass, apiVersion, wsdlUrl);
    }
      
    public static <P> P createPartnerProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion, final URL wsdlUrl) throws Exception {
        return createProxyPort(sessionMgr, WebServiceTypeEnum.PARTNER_SERVICE, serviceClass, apiVersion, wsdlUrl);
    }
      
    public static <P> P createToolingProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion, final URL wsdlUrl) throws Exception {
        return createProxyPort(sessionMgr, WebServiceTypeEnum.TOOLING_SERVICE, serviceClass, apiVersion, wsdlUrl);
    }    
    
    public static <P> P createCustomProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String name, final URL wsdlUrl) throws Exception {
        return createProxyPort(sessionMgr, WebServiceTypeEnum.CUSTOM_SERVICE, serviceClass, name, wsdlUrl);
    } 
    
    
    public static <P> P createEnterpriseProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final URL wsdlUrl) throws Exception {
        return createEnterpriseProxyPort(sessionMgr, serviceClass, sessionMgr.getSession().getCredentials().getApiVersion(), wsdlUrl);
    }
     
    public static <P> P createMetadataProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final URL wsdlUrl) throws Exception {
        return createMetadataProxyPort(sessionMgr, serviceClass, sessionMgr.getSession().getCredentials().getApiVersion(), wsdlUrl);
    }   
     
    public static <P> P createPartnerProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final URL wsdlUrl) throws Exception {
        return createPartnerProxyPort(sessionMgr,serviceClass, sessionMgr.getSession().getCredentials().getApiVersion(), wsdlUrl);
    }   
    
    public static <P> P createToolingProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final URL wsdlUrl) throws Exception {
        return createToolingProxyPort(sessionMgr, serviceClass, sessionMgr.getSession().getCredentials().getApiVersion(), wsdlUrl);
    }
     
          
    public static org.solenopsis.wsdls.enterprise.Soap createEnterpriseProxyPort(final SessionMgr sessionMgr, final String apiVersion, final URL wsdlUrl) throws Exception {
        return createEnterpriseProxyPort(sessionMgr, org.solenopsis.wsdls.enterprise.SforceService.class, apiVersion, wsdlUrl);
    }

    public static MetadataPortType createMetadataProxyPort(final SessionMgr sessionMgr, final String apiVersion, final URL wsdlUrl) throws Exception {
        return createMetadataProxyPort(sessionMgr, MetadataService.class, apiVersion, wsdlUrl);
    }
      
    public static org.solenopsis.wsdls.partner.Soap createPartnerProxyPort(final SessionMgr sessionMgr, final String apiVersion, final URL wsdlUrl) throws Exception {
        return createPartnerProxyPort(sessionMgr, org.solenopsis.wsdls.partner.SforceService.class, apiVersion, wsdlUrl);
    }
      
    public static SforceServicePortType createToolingProxyPort(final SessionMgr sessionMgr, final String apiVersion, final URL wsdlUrl) throws Exception {
        return createToolingProxyPort(sessionMgr, SforceServiceService.class, apiVersion, wsdlUrl);
    }   


    
    public static org.solenopsis.wsdls.enterprise.Soap createEnterpriseProxyPort(final SessionMgr sessionMgr, final URL wsdlUrl) throws Exception {
        return createEnterpriseProxyPort(sessionMgr, sessionMgr.getSession().getCredentials().getApiVersion(), wsdlUrl);
    }

    public static MetadataPortType createMetadataProxyPort(final SessionMgr sessionMgr, final URL wsdlUrl) throws Exception {
        return createMetadataProxyPort(sessionMgr, MetadataService.class, wsdlUrl);
    }

    public static org.solenopsis.wsdls.partner.Soap createPartnerProxyPort(final SessionMgr sessionMgr, final URL wsdlUrl) throws Exception {
        return createPartnerProxyPort(sessionMgr, org.solenopsis.wsdls.partner.SforceService.class, sessionMgr.getSession().getCredentials().getApiVersion(), wsdlUrl);
    }
    
    public static SforceServicePortType createToolingProxyPort(final SessionMgr sessionMgr, final URL wsdlUrl) throws Exception {
        return createToolingProxyPort(sessionMgr, SforceServiceService.class, sessionMgr.getSession().getCredentials().getApiVersion(), wsdlUrl);
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
    public static <P> P createProxyPort(final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass, final String name) throws Exception {
        return (P) (
            Proxy.newProxyInstance(
                SalesforceWebServiceUtil.class.getClassLoader(),
                SERVICE_MGR.createPort(serviceClass).getClass().getInterfaces(),
                new PortInvoker(sessionMgr, webServiceType, serviceClass, name)
            )
        );
    }

    public static <P> P createProxyPort(final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType, final Class<? extends Service> serviceClass) throws Exception {
        return createProxyPort(sessionMgr, webServiceType, serviceClass, SERVICE_MGR.getPortName(serviceClass));
    }

    
    public static <P> P createEnterpriseProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createProxyPort(sessionMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE, serviceClass, apiVersion);
    }
  
    public static <P> P createMetadataProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createProxyPort(sessionMgr, WebServiceTypeEnum.METADATA_SERVICE, serviceClass, apiVersion);
    }  
   
    public static <P> P createPartnerProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createProxyPort(sessionMgr, WebServiceTypeEnum.PARTNER_SERVICE, serviceClass, apiVersion);
    }     
    
    public static <P> P createToolingProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String apiVersion) throws Exception {
        return createProxyPort(sessionMgr, WebServiceTypeEnum.TOOLING_SERVICE, serviceClass, apiVersion);
    }   
     
    public static <P> P createCustomProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass, final String name) throws Exception {
        return createProxyPort(sessionMgr, WebServiceTypeEnum.CUSTOM_SERVICE, serviceClass, name);
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
    public static <P> P createEnterpriseProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass) throws Exception {
        return createEnterpriseProxyPort(sessionMgr, serviceClass, sessionMgr.getSession().getCredentials().getApiVersion());
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
    public static <P> P createMetadataProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass) throws Exception {
        return createMetadataProxyPort(sessionMgr, serviceClass, sessionMgr.getSession().getCredentials().getApiVersion());
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
    public static <P> P createPartnerProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass) throws Exception {
        return createPartnerProxyPort(sessionMgr, serviceClass, sessionMgr.getSession().getCredentials().getApiVersion());
    }
    
    public static <P> P createToolingProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass) throws Exception {
        return createToolingProxyPort(sessionMgr, serviceClass, sessionMgr.getSession().getCredentials().getApiVersion());
    }    
     
    public static <P> P createCustomProxyPort(final SessionMgr sessionMgr, Class<? extends Service> serviceClass) throws Exception {
        return createCustomProxyPort(sessionMgr, serviceClass, SERVICE_MGR.getPortName(serviceClass));
    }    
    
    
    public static org.solenopsis.wsdls.enterprise.Soap createEnterpriseProxyPort(final SessionMgr sessionMgr, final String apiVersion) throws Exception {
        return createEnterpriseProxyPort(sessionMgr, org.solenopsis.wsdls.enterprise.SforceService.class, apiVersion);
    }
     
    public static MetadataPortType createMetadataProxyPort(final SessionMgr sessionMgr, final String apiVersion) throws Exception {
        return createMetadataProxyPort(sessionMgr,MetadataService.class, apiVersion);
    }  
    
    public static org.solenopsis.wsdls.partner.Soap createPartnerProxyPort(final SessionMgr sessionMgr, final String apiVersion) throws Exception {
        return createEnterpriseProxyPort(sessionMgr, org.solenopsis.wsdls.partner.SforceService.class, apiVersion);
    }
    
    public static SforceServicePortType createToolingProxyPort(final SessionMgr sessionMgr, final String apiVersion) throws Exception {
        return createToolingProxyPort(sessionMgr, SforceServiceService.class, apiVersion);
    }      
    
    
    public static org.solenopsis.wsdls.enterprise.Soap createEnterpriseProxyPort(final SessionMgr sessionMgr) throws Exception {
        return createEnterpriseProxyPort(sessionMgr, org.solenopsis.wsdls.enterprise.SforceService.class);
    }
  
    public static MetadataPortType createMetadataProxyPort(final SessionMgr sessionMgr) throws Exception {
        return createMetadataProxyPort(sessionMgr, MetadataService.class);
    }
    
    public static org.solenopsis.wsdls.partner.Soap createPartnerProxyPort(final SessionMgr sessionMgr) throws Exception {
        return createEnterpriseProxyPort(sessionMgr, org.solenopsis.wsdls.partner.SforceService.class);
    }
    
    public static SforceServicePortType createToolingProxyPort(final SessionMgr sessionMgr) throws Exception {
        return createToolingProxyPort(sessionMgr, SforceServiceService.class);
    }
}
