
package org.solenopsis.lasius.wsimport.port.caller.impl;

import org.solenopsis.lasius.wsimport.port.SalesforceWebServiceContext;
import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Caller;


/**
 *
 * Abstract
 *
 */
public class SessionCallerDecorator<V> extends AbstractSessionCallerDecorator<V> {
    /**
     * Our Salesforce context.
     */
    private final SalesforceWebServiceContext context;

    /**
     * Return our context.
     */
    protected SalesforceWebServiceContext getContext() {
        return context;
    }

    /**
     * {@inheritDoc}
     */
    protected void createSession() throws Exception {
        getContext().createSession();
    }

    /**
     * Set the object for whom we decorate.
     *
     * @param decoratee is the object for whom we decorate.
     *
     * @throws IllegalArgumentException if decoratee is null.
     */
    public SessionCallerDecorator(final SalesforceWebServiceContext context, final Caller<V> decoratee) {
        super(decoratee);

        ParameterUtil.ensureParameter(context, "Context cannot be null!");

        this.context = context;
    }
}
