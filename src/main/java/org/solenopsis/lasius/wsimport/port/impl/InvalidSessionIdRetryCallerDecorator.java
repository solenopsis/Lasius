package org.solenopsis.lasius.wsimport.port.impl;

import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.Caller;
import org.flossware.util.reflect.impl.AbstractRetryCallerDecorator;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Allows retries to Salesforce calls if we have an invalid session id.
 *
 * @author sfloess
 *
 */
public class InvalidSessionIdRetryCallerDecorator<V> extends AbstractRetryCallerDecorator<V> {
    /**
     * Total number of retries is 1 for invalid session id.
     */
    public static final int MAX_RETRIES = 1;

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
        return MAX_RETRIES;
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    protected boolean isToRetryCall(final Call<V> call, final Exception failure) throws Exception {
        if (!SalesforceWebServiceUtil.isInvalidSessionId(failure)) {
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
