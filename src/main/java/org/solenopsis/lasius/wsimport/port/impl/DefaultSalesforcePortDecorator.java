package org.solenopsis.lasius.wsimport.port.impl;

import javax.xml.ws.Service;
import org.flossware.util.reflect.Caller;
import org.flossware.util.wsimport.port.PortDecorator;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * Default implementation of a port manager.
 *
 * @author sfloess
 *
 */
public class DefaultSalesforcePortDecorator extends AbstractPortDecorator {

    /**
     * Create a caller to manage the Salesforce calls.
     *
     * @param context is the state about our salesforce calls.
     *
     * @throws Exception if any problems arise generating our return value.
     */
    protected Caller createCaller(final Context context) {
        final SalesforceCaller salesforceCaller = new SalesforceCaller(context);
        final ConcurrentCallerDecorator concurrentCaller = new ConcurrentCallerDecorator(context, salesforceCaller);
        final SessionCallerDecorator sessionCaller = new SessionCallerDecorator(context, concurrentCaller);
        final InvalidSessionIdRetryCallerDecorator retryCaller = new InvalidSessionIdRetryCallerDecorator(context, sessionCaller);
        final PortFactoryCallerDecorator portFactoryCaller = new PortFactoryCallerDecorator(context, retryCaller);

        return portFactoryCaller;
    }

    /**
     * This constructor sets the session manager and decorator.
     *
     * @param decorator is the object for whom decoration will be created.
     *
     * @throws IllegalArgumentException if sessionMgr or decorator are null.
     */
    public DefaultSalesforcePortDecorator(final PortDecorator decorator) {
        super(decorator);
    }

    /**
     * This constructor sets the session manager.
     *
     * @param sessionMgr is the session manager.
     *
     * @throws IllegalArgumentException if sessionMgr is null.
     */
    public DefaultSalesforcePortDecorator() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createPort(final SessionMgr sessionMgr, WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name) throws Exception {
        return (P) getDecorator().createPort(service, portType, createCaller(new Context(webServiceType, service, portType, name, sessionMgr)));
    }
}
