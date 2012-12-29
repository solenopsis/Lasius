package org.solenopsis.lasius.wsimport.session.security.impl;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.wsimport.UrlUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.sforce.wsimport.enterprise.SforceService;
import org.solenopsis.lasius.sforce.wsimport.enterprise.Soap;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.security.SecurityWebSvc;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Abstract base class for security web services.
 *
 * @author sfloess
 *
 */
public abstract class AbstractSecurityWebSvc<S extends Service, P> implements SecurityWebSvc {
    /**
     * Our logger.
     */
    private final Logger logger;

    /**
     * Our web service type.
     */
    private final WebServiceTypeEnum webServiceType;

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
    private final S webService;

    /**
     * Return our web service.
     *
     * @return our web service.
     */
    protected S getWebService() {
        return webService;
    }

    /**
     * Return our web service type.
     *
     * @return our web service type.
     */
    protected WebServiceTypeEnum getWebServiceType() {
        return webServiceType;
    }

    /**
     * Create a port.
     *
     * @return a newly created port.
     */
    protected abstract P createPort();

    /**
     * Return a port.
     *
     * @param credentials contain data for connecting.
     *
     * @throws IllegalArgumentException if credentials is null.
     */
    protected P getPort(final Credentials credentials) {
        ParameterUtil.ensureParameter(credentials, "Cannot have null credentials!");

        final P port = createPort();

        UrlUtil.setUrl(port, credentials.getUrl() + "/" + getWebServiceType().getUrlSuffix() + "/" + credentials.getApiVersion());

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

        final P port = createPort();

        UrlUtil.setUrl(port, session.getServerUrl() + "/" + getWebServiceType().getUrlSuffix() + "/" + session.getCredentials().getApiVersion());

        SalesforceWebServiceUtil.setSessionId(port, getWebService(), session);

        return port;
    }

    /**
     * This constructor sets the web service type.
     *
     * @param service is the actual web service.
     * @param webServiceType is the web service type being used.
     *
     * @throws IllegalArgumentException if webServiceType is null.
     */
    protected AbstractSecurityWebSvc(final S service, final WebServiceTypeEnum webServiceType) {
        ParameterUtil.ensureParameter(webServiceType, "Web service type cannot be null!");

        this.logger         = Logger.getLogger(getClass().getName());
        this.webService     = service;
        this.webServiceType = webServiceType;
    }
}
