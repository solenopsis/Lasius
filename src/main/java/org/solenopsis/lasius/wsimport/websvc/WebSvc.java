package org.solenopsis.lasius.wsimport.websvc;

import javax.xml.ws.Service;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * Denotes a Salesforce web service.
 *
 * @param <P> the type of port.
 *
 * @author sfloess
 *
 */
public interface WebSvc<P> {
    /**
     * Return the web service type.
     *
     * @return the web service type.
     */
    public WebServiceTypeEnum getWebServiceType();

    /**
     * Return the service being used.
     *
     * @return the service being used.
     */
    public Service getService();

    /**
     * Return the name of the web service.
     *
     * @return the name of the service.
     */
    public String getName();

    /**
     * Create a vanilla port.
     *
     * @return a port with no end point data - just a vanilla port.
     */
    public P createPort();

    /**
     * Create a port using session.
     *
     * @param session the session to use.
     *
     * @return a port for web service calls.
     */
    public P createPort(Session session);

    /**
     * Create a port that uses sessionMgr for sessions.
     *
     * @param sessionMgr the session manager that manages sessions.
     *
     * @return a port for web service calls.
     *
     * @throws Exception if any problems arise creating the port.
     */
    public P createPort(SessionMgr sessionMgr) throws Exception;
}
