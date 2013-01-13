
package org.solenopsis.lasius.wsimport.port.caller.impl;

import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.Caller;
import org.flossware.util.reflect.impl.AbstractCallerDecorator;


/**
 *
 * Abstract
 *
 */
public abstract class AbstractSessionCallerDecorator<V> extends AbstractCallerDecorator<V> {
    /**
     * Will create a session.
     *
     * @throws Exception if any problems arise creating the session.
     */
    protected abstract void createSession() throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    protected Call<V> prepareCall(final Call<V> call) throws Throwable {
        createSession();

        return call;
    }

    /**
     * Set the object for whom we decorate.
     *
     * @param decoratee is the object for whom we decorate.
     *
     * @throws IllegalArgumentException if decoratee is null.
     */
    public AbstractSessionCallerDecorator(final Caller<V> decoratee) {
        super(decoratee);
    }
}
