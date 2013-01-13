package org.solenopsis.lasius.wsimport.port;

import javax.xml.ws.Service;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * This interface defines the API for decorating SFDC web service ports.  For example,
 * for session limits per login, retries, etc.
 *
 * @author sfloess
 *
 */
public interface SalesforcePortDecorator {
    /**
     * Generate a port for use.
     *
     * @param <P> the type of port we will decorate.
     *
     * @param context the context for salesforce web service calls.
     *
     * @return a port with decorated functionality like limited concurrent access to SFDC.
     *
     * @throws Exception if any problems arise generating our return value.
     */
    <P> P createPort(SalesforceWebServiceContext context) throws Exception;


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
    <P> P createPort(WebServiceTypeEnum webServiceType, Service service, Class<P> portType, String name, SessionMgr sessionMgr) throws Exception;
}
