
package org.solenopsis.lasius.wsimport.port.impl;

import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.Caller;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Will perform calls to Salesforce web services.
 *
 * @author sfloess
 *
 */
public class SalesforceCaller<V> implements Caller<V> {
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
     * This constructor sets up the web service type, name and max retries for managing
     * calls to Salesforce.
     *
     * @param webServiceType is the type of web service being decorated.
     * @param name the name of the web service.
     *
     * @return a port with decorated functionality like limited concurrent access to SFDC.
     *
     * @throws Exception if any problems arise generating our return value.
     */
    public SalesforceCaller(final Context context) {
        ParameterUtil.ensureParameter(context, "Cannot have a null context!");

        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object executeCall(final Call<V> call) throws Throwable {
        SalesforceWebServiceUtil.setUrl(call.getObject(), getContext().getSession().getServerUrl(), getContext().getWebServiceType(), getContext().getName());
        SalesforceWebServiceUtil.setSessionId(call.getObject(), getContext().getService(), getContext().getSession());

        return call.execute();
    }
}
