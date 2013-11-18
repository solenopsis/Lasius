package org.solenopsis.lasius.wsimport.call;

import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.UrlUtil;
import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.layering.impl.AbstractLayeringCaller;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Will perform calls on a Salesforce port.
 *
 * @author sfloess
 *
 */
public class SalesforceCaller extends AbstractLayeringCaller<Session> {
    /**
     * The session manager.
     */
    private final SessionMgr sessionMgr;

    /**
     * The type of web service.
     */
    private final WebServiceTypeEnum webServiceType;

    /**
     * The web service.
     */
    private final Service service;

    /**
     * The port type.
     */
    private final Class<?> portType;

    /**
     * The name of the web service.
     */
    private final String name;

    /**
     * Return the session manager.
     *
     * @return the session manager.
     */
    protected SessionMgr getSessionMgr() {
        return sessionMgr;
    }

    /**
     * Return the type of web service.
     *
     * @return the type of web service.
     */
    protected WebServiceTypeEnum getWebServiceType() {
        return webServiceType;
    }

    /**
     * Return the web service.
     *
     * @return the web service.
     */
    protected Service getService() {
        return service;
    }

    /**
     * Return the port type.
     *
     * @return the port type.
     */
    protected Class<?> getPortType() {
        return portType;
    }

    /**
     * Return the name of the web service.
     *
     * @return the name of the web service.
     */
    protected String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Call<Session> prepareToCall(Call<Session> call) throws Exception {
        call.setValue(getSessionMgr().getSession());
        call.setObject(getService().getPort(portType));

        SalesforceWebServiceUtil.preparePort(getService(), call.getObject(), getName(), getWebServiceType(), call.getValue());

        call.getValue().getSemaphore().acquire();

        return call;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void callSucceeded(Call<Session> call) throws Exception {
        call.getValue().getSemaphore().release();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void callFailed(Call<Session> call, Throwable failure) throws Exception {
        if (SalesforceWebServiceUtil.isReloginException(failure)) {
            call.getValue().getSemaphore().release();
            getSessionMgr().resetSession(call.getValue());
        }
    }

    public SalesforceCaller(final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType, final Service service, final Class<?> portType, final String name) {
        this.sessionMgr     = ParameterUtil.ensureParameter(sessionMgr, "Cannot have a null session mgr!");
        this.webServiceType = ParameterUtil.ensureParameter(webServiceType, "Cannot have a null web service type!");
        this.service        = ParameterUtil.ensureParameter(service,  "Cannot have a null service!");
        this.portType       = ParameterUtil.ensureParameter(portType, "Cannot have a null port type!");
        this.name           = ParameterUtil.ensureParameter(name, "Cannot have a null/empty name!");
    }

}
