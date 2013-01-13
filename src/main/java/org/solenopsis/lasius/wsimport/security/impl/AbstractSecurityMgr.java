package org.solenopsis.lasius.wsimport.security.impl;

import java.util.logging.Logger;
import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.security.SecurityMgr;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Abstract base class for security web services.
 *
 * @author sfloess
 *
 */
public abstract class AbstractSecurityMgr implements SecurityMgr {
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
     * Return a port.
     *
     * @param credentials contain data for connecting.
     *
     * @throws IllegalArgumentException if credentials is null.
     */
    protected <P> P preparePort(final Credentials credentials, final P port, final WebServiceTypeEnum webServiceType) {
        ParameterUtil.ensureParameter(credentials, "Cannot have null credentials!");

        SalesforceWebServiceUtil.setUrl(port, credentials, webServiceType, credentials.getApiVersion());

        return port;
    }

    /**
     * Return a port.
     *
     * @param session contain data for connecting.
     *
     * @throws IllegalArgumentException if session is null.
     */
    protected <P> P preparePort(final Session session, final Service service, final P port, final WebServiceTypeEnum webServiceType) {
        ParameterUtil.ensureParameter(session, "Cannot have a null session!");

        SalesforceWebServiceUtil.setUrl(port, session, webServiceType, session.getCredentials().getApiVersion());
        SalesforceWebServiceUtil.setSessionId(port, service, session);

        return port;
    }

    /**
     * Default constructor.
     *
     * @param webSvc is the actual web service.
     *
     * @throws IllegalArgumentException if webServiceType is null.
     */
    protected AbstractSecurityMgr() {
        this.logger = Logger.getLogger(getClass().getName());
    }
}
