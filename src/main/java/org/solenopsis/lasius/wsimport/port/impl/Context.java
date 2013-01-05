package org.solenopsis.lasius.wsimport.port.impl;

import javax.xml.ws.Service;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * Represents our SFDC call context.
 *
 * @author sfloess
 *
 */
public class Context {
    /**
     * The type of web service being decorated.
     */
    private final WebServiceTypeEnum webServiceType;

    /**
     * The SFDC service being called.
     */
    private final Service service;

    /**
     * The port type on the service being called.
     */
    private final Class portType;

    /**
     * The name of the web service being called.
     */
    private final String name;

    /**
     * Session manager being used.
     */
    private final SessionMgr sessionMgr;

    /**
     * Current session being used.
     */
    private Session session;

    /**
     * This constructor sets the service, port and session manager being used.
     *
     * @param webServiceType is the type of web service being decorated.
     * @param service is the web service to use.
     * @param portType the web service port type to use.
     * @param name the name of the web service.
     * @param sessionMgr the session manager being used.
     */
    public <P> Context(final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, String name, final SessionMgr sessionMgr) {
        this.webServiceType = webServiceType;
        this.service        = service;
        this.portType       = portType;
        this.name           = name;
        this.sessionMgr     = sessionMgr;
    }

    /**
     * Return the web service type.
     *
     * @return the web service type.
     */
    public WebServiceTypeEnum getWebServiceType() {
        return webServiceType;
    }

    /**
     * Return the SFDC service.
     *
     * @return the SFDC service.
     */
    public Service getService() {
        return service;
    }

    /**
     * Return the port type.
     *
     * @return the port type.
     */
    public <P> Class<P> getPortType() {
        return portType;
    }

    /**
     * Return the web service name.
     *
     * @return the web service name.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the session manager being used.
     *
     * @return the session manager being used.
     */

    public SessionMgr getSessionMgr() {
        return sessionMgr;
    }

    /**
     * Will create a session.
     *
     * @return a created session.
     *
     * @throws Exception if any problems arise creating the session.
     */
    public Session createSession() throws Exception {
        session = getSessionMgr().getSession();

        return session;
    }

    /**
     * Return our current session.
     *
     * @return our current session.
     */
    public Session getSession() {
        return session;
    }

    /**
     * Reset the session being used.
     *
     * @return the reset session.
     *
     * @throws Exception  if any problems arise resetting the session.
     */
    public Session resetSession() throws Exception {
        session = getSessionMgr().resetSession(getSession());

        return session;
    }
}
