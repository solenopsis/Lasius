
package org.solenopsis.lasius.wsimport.port.call.impl;

import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.decorate.call.impl.AbstractDecorateCall;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Decorates calls to Salesforce by setting the session id.
 *
 * @author sfloess
 *
 */
public class SessionIdDecorateCall extends AbstractDecorateCall<Session> {
    /**
     * The web service.
     */
    private final Service service;

    /**
     * Return the web service.
     *
     * @return the web service.
     */
    protected Service getService() {
        return service;
    }

    /**
     * Default constructor
     */
    public SessionIdDecorateCall(final Service service) {
        ParameterUtil.ensureParameter(service, "Cannot have a null service!");

        this.service = service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Call<Session> decorate(final Call<Session> call) throws Exception {
        SalesforceWebServiceUtil.setSessionId(call.getObject(), getService(), call.getValue().getSessionId());

        return call;
    }
}
