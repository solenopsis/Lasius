package org.solenopsis.lasius.wsimport.port.caller.impl;

import org.flossware.util.reflect.Caller;
import org.solenopsis.lasius.wsimport.port.SalesforceWebServiceContext;

/**
 *
 * Default implementation of a SalesforceCallerFactory.
 *
 * @author sfloess
 *
 */
public final class DefaultSalesforceCallerFactory extends AbstractSalesforceCallerFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public Caller createCaller(final SalesforceWebServiceContext context) throws Exception {
        final SalesforceCaller salesforceCaller = new SalesforceCaller(context);
        final ConcurrentCallerDecorator concurrentCaller = new ConcurrentCallerDecorator(context, salesforceCaller);
        final SessionCallerDecorator sessionCaller = new SessionCallerDecorator(context, concurrentCaller);
        final InvalidSessionIdRetryCallerDecorator retryCaller = new InvalidSessionIdRetryCallerDecorator(context, sessionCaller);
        final PortFactoryCallerDecorator portFactoryCaller = new PortFactoryCallerDecorator(context, retryCaller);

        return portFactoryCaller;
    }
}
