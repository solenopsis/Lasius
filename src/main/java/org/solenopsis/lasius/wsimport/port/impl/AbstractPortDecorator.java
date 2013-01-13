package org.solenopsis.lasius.wsimport.port.impl;

import java.util.logging.Logger;
import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.wsimport.port.PortDecorator;
import org.flossware.util.wsimport.port.impl.DefaultPortDecorator;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.port.SalesforcePortDecorator;
import org.solenopsis.lasius.wsimport.port.SalesforceWebServiceContext;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * Abstract base class for port decorators.
 *
 * @author sfloess
 *
 */
public abstract class AbstractPortDecorator implements SalesforcePortDecorator {
    /**
     * The default port decorator.
     */
    private static final PortDecorator DEFAULT_PORT_DECORATOR = new DefaultPortDecorator();

    /**
     * Our logger.
     */
    private final Logger logger;

    /**
     * The object that can perform decoration.
     */
    private final PortDecorator decorator;

    /**
     * Return the default port decorator.
     *
     * @return the default port decorator.
     */
    protected static PortDecorator getDefaultPortDecorator() {
        return DEFAULT_PORT_DECORATOR;
    }

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected final Logger getLogger() {
        return logger;
    }

    /**
     * Return our decorator.
     *
     * @return the decorator.
     */
    protected PortDecorator getDecorator() {
        return decorator;
    }

    /**
     * This constructor sets the session manager and decorator.
     *
     * @param decorator is the object for whom decoration will be created.
     *
     * @throws IllegalArgumentException if decorator is null.
     */
    protected AbstractPortDecorator(final PortDecorator decorator) {
        ParameterUtil.ensureParameter(decorator, "Decorator cannot be null!");

        this.logger    = Logger.getLogger(getClass().getName());
        this.decorator = decorator;
    }

    /**
     * This constructor sets the session manager.  It will use a default decorator.
     */
    protected AbstractPortDecorator() {
        this(getDefaultPortDecorator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createPort(WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name, final SessionMgr sessionMgr) throws Exception {
        return createPort(new SalesforceWebServiceContext(webServiceType, service, portType, name, sessionMgr));
    }
}
