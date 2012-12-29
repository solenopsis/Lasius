
package org.solenopsis.lasius.wsimport.port.impl;

import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Caller;
import org.flossware.util.wsimport.AbstractPortFactoryCallerDecorator;


/**
 *
 * This caller decorates by creating a new port for each call.
 *
 */
public class PortFactoryCallerDecorator<V> extends AbstractPortFactoryCallerDecorator<V> {
    /**
     * Our Salesforce context.
     */
    private final Context context;

    /**
     * Return our context.
     */
    protected Context getContext() {
        return context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Service getService() {
        return getContext().getService();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <P> Class<P> getPortType() {
        return getContext().getPortType();
    }

    /**
     * Set the object for whom we decorate.
     *
     * @param decoratee is the object for whom we decorate.
     *
     * @throws IllegalArgumentException if decoratee is null.
     */
    public PortFactoryCallerDecorator(final Context context, final Caller<V> decoratee) {
        super(decoratee);

        ParameterUtil.ensureParameter(context, "Context cannot be null!");

        this.context = context;
    }
}
