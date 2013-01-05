package org.solenopsis.lasius.wsimport.websvc.impl;

import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.port.SalesforcePortDecorator;
import org.solenopsis.lasius.wsimport.port.SalesforcePortFactory;
import org.solenopsis.lasius.wsimport.port.impl.DefaultSalesforcePortDecorator;
import org.solenopsis.lasius.wsimport.port.impl.DefaultSalesforcePortFactory;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;
import org.solenopsis.lasius.wsimport.websvc.WebSvc;

/**
 *
 * Abstract base class for Salesforce web services.
 *
 * @author sfloess
 *
 */
public abstract class AbstractWebSvc<P> implements WebSvc<P> {
    /**
     * Our default port decorator.
     */
    protected static final SalesforcePortDecorator DEFAULT_PORT_DECORATOR = new DefaultSalesforcePortDecorator();

    /**
     * Our default port factory.
     */
    protected static final SalesforcePortFactory DEFAULT_PORT_FACTORY = new DefaultSalesforcePortFactory();

    /**
     * The default Salesforce API version.
     */
    protected static final String DEFAULT_API_VERSION = "25.0";

    /**
     * Will decorate ports - for example with auto logins and re-logins.
     */
    private final SalesforcePortDecorator portDecorator;

    /**
     * Creates a port for use.
     */
    private final SalesforcePortFactory portFactory;

    /**
     * The type of Salesforce web service (enterprise, partner, metadata, or custom).
     */
    private final WebServiceTypeEnum webServiceType;

    /**
     * The Salesforce web service.
     */
    private final Service service;

    /**
     * The type of port on the web service.
     */
    private final Class<P> portType;

    /**
     * The name of the web service.  For enterprise, partner and metadata web services,
     * it is the api version.
     */
    private final String name;

    /**
     * Return the port decorator.
     *
     * @return the port decorator.
     */
    protected SalesforcePortDecorator getPortDecorator() {
        return portDecorator;
    }

    /**
     * Return the port factory.
     *
     * @return the port factory.
     */
    protected SalesforcePortFactory getPortFactory() {
        return portFactory;
    }

    /**
     * Return the type of port being used.
     *
     * @return the type of port being used.
     */
    protected Class<P> getPortType() {
        return portType;
    }

    /**
     * This constructor sets the port decorator and factory, web service type, the web service and the port to be used as well as the web service name.
     */
    protected AbstractWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name) {
        ParameterUtil.ensureParameter(portDecorator, "Port decorator cannot be null!");
        ParameterUtil.ensureParameter(portFactory, "Port factory cannot be null!");
        ParameterUtil.ensureParameter(webServiceType, "Web service type cannot be null!");
        ParameterUtil.ensureParameter(service, "Service cannot be null!");
        ParameterUtil.ensureParameter(portType, "Port type cannot be null!");
        ParameterUtil.ensureParameter(name, "Web service name cannot be null or empty!");

        this.portDecorator  = portDecorator;
        this.portFactory    = portFactory;
        this.webServiceType = webServiceType;
        this.service        = service;
        this.portType       = portType;
        this.name           = name;
    }

    /**
     * This constructor sets the web service type, the web service and the port to be used as well as the web service name.
     */
    protected AbstractWebSvc(final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name) {
        this(DEFAULT_PORT_DECORATOR, DEFAULT_PORT_FACTORY, webServiceType, service, portType, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebServiceTypeEnum getWebServiceType() {
        return webServiceType;
    }

    /**
     * {@inheritDoc}
     */
    public Service getService() {
        return service;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public P createPort() {
        return getService().getPort(getPortType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public P createPort(final Session session) {
        return getPortFactory().createPort(session, getWebServiceType(), getService(), getPortType(), getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public P createPort(final SessionMgr sessionMgr) throws Exception {
        return getPortDecorator().createPort(sessionMgr, getWebServiceType(), getService(), getPortType(), getName());
    }
}
