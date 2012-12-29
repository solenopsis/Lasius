package org.solenopsis.lasius.wsimport.port.impl;

import org.flossware.util.reflect.Caller;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * Default implementation of a port manager.
 *
 * @author sfloess
 *
 */
public class DefaultPortMgr extends AbstractPortMgr {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Caller createCaller(final Context context) {
        final SalesforceCaller salesforceCaller = new SalesforceCaller(context);
        final ConcurrentCallerDecorator concurrentCaller = new ConcurrentCallerDecorator(context, salesforceCaller);
        final InvalidSessionIdRetryCallerDecorator retryCaller = new InvalidSessionIdRetryCallerDecorator(context, concurrentCaller);
        final PortFactoryCallerDecorator portFactoryCaller = new PortFactoryCallerDecorator(context, retryCaller);
        
        return portFactoryCaller;
    }

    /**
     * This constructor sets the session manager.
     *
     * @param sessionMgr is the session manager.
     *
     * @throws IllegalArgumentException if sessionMgr is null.
     */
    public DefaultPortMgr(final SessionMgr sessionMgr) {
        super(sessionMgr);
    }
}
