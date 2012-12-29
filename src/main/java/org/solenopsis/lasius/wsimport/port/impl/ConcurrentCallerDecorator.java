
package org.solenopsis.lasius.wsimport.port.impl;

import java.util.concurrent.Semaphore;
import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Caller;
import org.flossware.util.reflect.impl.AbstractConcurrentCallerDecorator;


/**
 *
 * Abstract base class for callers requiring limiting concurrent access.
 *
 */
public class ConcurrentCallerDecorator<V> extends AbstractConcurrentCallerDecorator<V> {
    /**
     * Our context.
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
    protected Semaphore getSemaphore() {
        return getContext().getSession().getSemaphore();
    }

    /**
     * Set up the caller for whom we are adding this embellishment.
     *
     * @param decoratee is the object for whom we are limiting simultaneous calls.
     *
     * @throws IllegalArgumentException if <code>decoratee</code> is null.
     */
    public ConcurrentCallerDecorator(final Context context, final Caller<V> caller) {
        super(caller);

        ParameterUtil.ensureParameter(context, "Cannot have a null context!");

        this.context = context;
    }
}
