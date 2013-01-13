package org.solenopsis.lasius.wsimport.port.impl;

import org.solenopsis.lasius.wsimport.port.caller.impl.SalesforceCaller;
import org.solenopsis.lasius.wsimport.port.caller.impl.SessionCallerDecorator;
import org.solenopsis.lasius.wsimport.port.caller.impl.PortFactoryCallerDecorator;
import org.solenopsis.lasius.wsimport.port.caller.impl.ConcurrentCallerDecorator;
import org.solenopsis.lasius.wsimport.port.caller.impl.InvalidSessionIdRetryCallerDecorator;
import org.solenopsis.lasius.wsimport.port.SalesforceWebServiceContext;
import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Caller;
import org.flossware.util.wsimport.port.PortDecorator;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.port.caller.SalesforceCallerFactory;
import org.solenopsis.lasius.wsimport.port.caller.impl.DefaultSalesforceCallerFactory;
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
     * The default caller factory.
     */
    private static final SalesforceCallerFactory DEFAULT_CALLER_FACTORY = new DefaultSalesforceCallerFactory();

    /**
     * Our caller factory.
     */
    private final SalesforceCallerFactory callerFactory;

    /**
     * Return our default caller factory.
     *
     * @return our default caller factory.
     */
    protected static SalesforceCallerFactory getDefaultCallerFactory() {
        return DEFAULT_CALLER_FACTORY;
    }

    /**
     * This constructor sets the port decorator and caller factory.
     *
     * @param decorator can create ports that are decorated with callers.
     * @param callerFactory creates callers based upon a web service context.
     *
     * @throws IllegalArgumentException if decorator or caller factory are null.
     */
    public DefaultSalesforcePortDecorator(final PortDecorator decorator, final SalesforceCallerFactory callerFactory) {
        super(decorator);

        ParameterUtil.ensureParameter(callerFactory, "The caller factory cannot be null!");

        this.callerFactory = callerFactory;
    }

    /**
     * This constructor sets the port decorator and caller factory.
     *
     * @param decorator can create ports that are decorated with callers.
     *
     * @throws IllegalArgumentException if decorator is null.
     */
    public DefaultSalesforcePortDecorator(final PortDecorator decorator) {
        this(decorator, getDefaultCallerFactory());
    }

    /**
     * This constructor sets the caller factory.
     *
     * @param callerFactory creates callers based upon a web service context.
     *
     * @throws IllegalArgumentException if caller factory are null.
     */
    public DefaultSalesforcePortDecorator(final SalesforceCallerFactory callerFactory) {
        this(getDefaultPortDecorator(), callerFactory);
    }

    /**
     * Default constructor will use a default port decorator and caller factory.
     */
    public DefaultSalesforcePortDecorator() {
        this(getDefaultPortDecorator(), getDefaultCallerFactory());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createPort(final SalesforceWebServiceContext context) throws Exception {
        return (P) getDecorator().createPort(context.getService(), context.getPortType(), getDefaultCallerFactory().createCaller(context));
    }
}
