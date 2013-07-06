
package org.solenopsis.lasius.wsimport.port.call.impl;

import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.decorate.call.impl.AbstractDecorateCall;
import org.solenopsis.lasius.wsimport.session.Session;

/**
 *
 * Decorates calls to Salesforce by setting the port.
 *
 * @author sfloess
 *
 */
public class CreatePortDecorateCall extends AbstractDecorateCall<Session> {
    /**
     * The web service.
     */
    private final Service service;

    /**
     * The port type.
     */
    private final Class<?> portType;

    /**
     * Return the web service.
     *
     * @return the web service.
     */
    protected Service getService() {
        return service;
    }

    /**
     * Return the port type.
     *
     * @return the port type.
     */
    protected Class<?> getPortType() {
        return portType;
    }

    /**
     * Default constructor
     */
    public CreatePortDecorateCall(final Service service, final Class<?> portType) {
        ParameterUtil.ensureParameter(service,  "Cannot have a null service!");
        ParameterUtil.ensureParameter(portType, "Cannot have a null port type!");

        this.service  = service;
        this.portType = portType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Call<Session> decorate(final Call<Session> call) throws Exception {
        call.setObject(getService().getPort(portType));

        return call;
    }
}
