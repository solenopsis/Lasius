package org.solenopsis.lasius.wsimport.port.impl;

import java.lang.reflect.InvocationTargetException;
import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.Caller;
import org.flossware.util.reflect.impl.AbstractRetryCallerDecorator;

/**
 *
 * Allows retries to Salesforce calls if we have an invalid session id.
 *
 * @author sfloess
 *
 */
public class InvalidSessionIdRetryCallerDecorator<V> extends AbstractRetryCallerDecorator<V> {
    /**
     * Denotes an invalid session id.
     */
    public static final String INVALID_SESSION_ID = "INVALID_SESSION_ID";

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
    protected int getMaxRetries() {
        return getContext().getMaxRetries();
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    protected boolean isInvalidSessionId(final Exception failure) {
        if (failure instanceof InvocationTargetException) {
            return ((InvocationTargetException) failure).getTargetException().getMessage().contains(INVALID_SESSION_ID);
        }

        return failure.getMessage().contains(INVALID_SESSION_ID);
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    protected boolean isToRetryCall(final Call<V> call, final Exception failure) throws Exception {
        if (!isInvalidSessionId(failure)) {
            return false;
        }

        getContext().resetSession();

        return true;
    }

    /**
     * Set the number of retries when failure arises.
     *
     * @param maxRetries total number of retries allowed.
     */
    public InvalidSessionIdRetryCallerDecorator(final Context context, final Caller<V> caller) {
        super (caller);

        ParameterUtil.ensureParameter(context, "Cannot have a null context!");

        this.context = context;
    }
}
