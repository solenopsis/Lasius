package org.solenopsis.lasius.wsimport.session.security.impl;

import java.util.logging.Logger;
import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.wsimport.UrlUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.security.SecurityWebSvc;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;
import org.solenopsis.lasius.wsimport.websvc.WebSvc;

/**
 *
 * Abstract base class for security web services.
 *
 * @author sfloess
 *
 */
public abstract class AbstractSecurityWebSvc<P> implements SecurityWebSvc {
    /**
     * Our logger.
     */
    private final Logger logger;

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected Logger getLogger() {
        return logger;
    }

    /**
     * The service being used.
     */
    private final WebSvc<P> webService;

    /**
     * Return our web service.
     *
     * @return our web service.
     */
    protected WebSvc<P> getWebService() {
        return webService;
    }

    /**
     * Return a port.
     *
     * @param credentials contain data for connecting.
     *
     * @throws IllegalArgumentException if credentials is null.
     */
    protected P getPort(final Credentials credentials) {
        ParameterUtil.ensureParameter(credentials, "Cannot have null credentials!");

        final P port = getWebService().createPort();

        SalesforceWebServiceUtil.setUrl(port, credentials, getWebService().getWebServiceType(), credentials.getApiVersion());

        return port;
    }

    /**
     * Return a port.
     *
     * @param session contain data for connecting.
     *
     * @throws IllegalArgumentException if session is null.
     */
    protected P getPort(final Session session) {
        ParameterUtil.ensureParameter(session, "Cannot have a null session!");

        final P port = getWebService().createPort();

        SalesforceWebServiceUtil.setUrl(port, session, getWebService().getWebServiceType(), session.getCredentials().getApiVersion());
        SalesforceWebServiceUtil.setSessionId(port, getWebService(), session);

        return port;
    }

    /**
     * This constructor sets the web service type.
     *
     * @param webSvc is the actual web service.
     *
     * @throws IllegalArgumentException if webServiceType is null.
     */
    protected AbstractSecurityWebSvc(final WebSvc webSvc) {
        ParameterUtil.ensureParameter(webSvc, "Web service cannot be null!");

        this.logger     = Logger.getLogger(getClass().getName());
        this.webService = webSvc;
    }
}
