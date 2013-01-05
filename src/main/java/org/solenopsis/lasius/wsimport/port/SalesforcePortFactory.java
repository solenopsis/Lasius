package org.solenopsis.lasius.wsimport.port;

import javax.xml.ws.Service;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;

/**
 *
 * This interface defines the API for decorating SFDC web service ports.  For example,
 * for session limits per login, retries, etc.
 *
 * @author sfloess
 *
 */
public interface SalesforcePortFactory {
    /**
     * Create a port using session.
     *
     * @param <P> the type of port to use.
     *
     * @param session is the session for whom we will call to SFDC using port.
     * @param webServiceType is the type of web service being decorated.
     * @param service is the web service to use.
     * @param portType the class of the port type.
     * @param name the name of the web service.
     *
     * @return a port ready to be used.
     */
    <P> P createPort(final Session session, WebServiceTypeEnum webServiceType, Service service, Class<P> portType, String name);
}
