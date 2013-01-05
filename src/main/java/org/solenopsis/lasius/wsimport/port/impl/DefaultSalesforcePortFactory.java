package org.solenopsis.lasius.wsimport.port.impl;

import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.wsimport.port.PortFactory;
import org.flossware.util.wsimport.port.impl.DefaultPortFactory;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.port.SalesforcePortFactory;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Default implementation of a port manager.
 *
 * @author sfloess
 *
 */
public class DefaultSalesforcePortFactory extends AbstractPortMgr implements SalesforcePortFactory {
    /**
     * Our port factory.
     */
    private final PortFactory portFactory;

    /**
     * Return the port factory.
     */
    protected PortFactory getPortFactory() {
        return portFactory;
    }

    /**
     * Set the port factory to use.
     *
     * @param portFactory the port factory to use.
     */
    public DefaultSalesforcePortFactory(final PortFactory portFactory) {
        ParameterUtil.ensureParameter(portFactory, "Cannot have a null port factory!");

        this.portFactory = portFactory;
    }

    /**
     * Default constructor.  Will use a default port factory.
     */
    public DefaultSalesforcePortFactory() {
        this(new DefaultPortFactory());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createPort(final Session session, WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name) {
        final P port = getPortFactory().createPort(service, portType, SalesforceWebServiceUtil.computeUrl(session, webServiceType, name));

        SalesforceWebServiceUtil.setSessionId(port, service, session);

        return port;
    }
}
