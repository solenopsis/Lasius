package org.solenopsis.lasius.wsimport.port.impl;

import java.util.logging.Logger;
import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Caller;
import org.flossware.util.reflect.decorator.Decorator;
import org.flossware.util.reflect.decorator.impl.DefaultDecorator;
import org.flossware.util.wsimport.PortUtil;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.port.PortMgr;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * Abstract base class for port managers.
 *
 * @author sfloess
 *
 */
public abstract class AbstractPortMgr implements PortMgr {
    /**
     * Our logger.
     */
    private final Logger logger;

    /**
     * The session manager.
     */
    private final SessionMgr sessionMgr;

    /**
     * The object that can perform decoration.
     */
    private final Decorator decorator;

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected final Logger getLogger() {
        return logger;
    }

    /**
     * Return the session manager.
     *
     * @return the session manager.
     */
    protected SessionMgr getSessionMgr() {
        return sessionMgr;
    }

    /**
     * Return our decorator.
     *
     * @return the decorator.
     */
    protected Decorator getDecorator() {
        return decorator;
    }

    /**
     * Create a caller to manage the Salesforce calls.
     *
     * @param context is the state about our salesforce calls.
     *
     * @throws Exception if any problems arise generating our return value.
     */
    protected abstract Caller createCaller(Context context);

    /**
     * This constructor sets the session manager and decorator.
     *
     * @param sessionMgr is the session manager.
     * @param decorator is the object for whom decoration will be created.
     *
     * @throws IllegalArgumentException if sessionMgr or decorator are null.
     */
    protected AbstractPortMgr(final SessionMgr sessionMgr, final Decorator decorator) {
        ParameterUtil.ensureParameter(sessionMgr, "Session manager cannot be null!");
        ParameterUtil.ensureParameter(decorator, "Decorator cannot be null!");

        this.logger     = Logger.getLogger(getClass().getName());
        this.sessionMgr = sessionMgr;
        this.decorator  = decorator;
    }

    /**
     * This constructor sets the session manager.  It will use a default decorator.
     *
     * @param sessionMgr is the session manager.
     *
     * @throws IllegalArgumentException if sessionMgris null.
     */
    protected AbstractPortMgr(final SessionMgr sessionMgr) {
        this(sessionMgr, new DefaultDecorator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createPort(WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name, final int maxRetries) throws Exception {
        return (P) getDecorator().decorate(PortUtil.createPort(service, portType), createCaller(new Context(webServiceType, service, portType, name, maxRetries, getSessionMgr())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createPort(WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name) throws Exception {
        return createPort(webServiceType, service, portType, name, DEFAULT_MAX_RETRIES);
    }
}
