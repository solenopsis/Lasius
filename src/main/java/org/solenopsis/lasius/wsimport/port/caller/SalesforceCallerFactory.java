package org.solenopsis.lasius.wsimport.port.caller;

import org.flossware.util.reflect.Caller;
import org.solenopsis.lasius.wsimport.port.SalesforceWebServiceContext;

/**
 *
 * Will create a caller based upon a context.
 *
 * @author sfloess
 *
 */
public interface SalesforceCallerFactory<V> {
    /**
     * Create a caller based upon a context.
     *
     * @param context the context to use when creating a caller.
     *
     * @return a caller.
     *
     * @throws Exception if any problems arise creating the caller.
     */
    Caller<V> createCaller(SalesforceWebServiceContext context) throws Exception;
}
