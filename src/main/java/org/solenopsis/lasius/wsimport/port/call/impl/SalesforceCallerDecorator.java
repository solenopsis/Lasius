
package org.solenopsis.lasius.wsimport.port.call.impl;

import java.util.concurrent.Semaphore;
import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.Caller;
import org.flossware.util.reflect.decorate.call.DecorateCall;
import org.flossware.util.reflect.decorate.impl.AbstractSempahoreConcurrentCallerDecorator;
import org.solenopsis.lasius.wsimport.session.Session;



/**
 *
 * Performs the calls to SFDC, limiting concurrent access.  Can be used in conjunction with a decorated caller.
 *
 */
public class SalesforceCallerDecorator extends AbstractSempahoreConcurrentCallerDecorator<Session> {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Semaphore getSemaphore(Call<Session> call) {
        return call.getValue().getSemaphore();
    }

    /**
     * Set the caller for whom we will decorate.
     *
     * @param decoratee the caller for whom we will add embellishment.
     * @param decorateCall the object who can take a call and decorate it with embellished behavior.
     *
     * @throws IllegalArgumentException if caller or decorateCall are null.
     */
    public SalesforceCallerDecorator(final Caller<Session> decoratee, final DecorateCall decorateCall) {
        super(decoratee, decorateCall);
    }

    /**
     * Set the object to decorate calls.
     *
     * @param decorateCall the object that will decorate calls.
     *
     * @throws IllegalArgumentException if decorateCall is null.
     */
    public SalesforceCallerDecorator(final DecorateCall<Session> decorateCall) {
        super(decorateCall);
    }

    /**
     * Set the decorated object.
     *
     * @param decoratee is the object for whom we are limiting simultaneous calls.
     *
     * @throws IllegalArgumentException if <code>decoratee</code> is null.
     */
    public SalesforceCallerDecorator(final Caller<Session> decoratee) {
        super(decoratee);
    }

    /**
     * Default constructor.
     */
    public SalesforceCallerDecorator() {
    }
}
