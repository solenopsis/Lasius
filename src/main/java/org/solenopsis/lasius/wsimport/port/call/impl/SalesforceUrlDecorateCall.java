
package org.solenopsis.lasius.wsimport.port.call.impl;

import javax.xml.ws.Service;
import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Call;
import org.flossware.util.wsimport.call.impl.AbstractUrlDecorateCall;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Decorates calls to Salesforce by setting the URL, web service name and session id.
 *
 * @author sfloess
 *
 */
public class SalesforceUrlDecorateCall extends AbstractUrlDecorateCall<Session> {
    /**
     * The web service.
     */
    private final Service service;

    /**
     * The name of the web service.
     */
    private final String name;

    /**
     * The type of web service.
     */
    private final WebServiceTypeEnum webServiceType;

    /**
     * Return the web service.
     *
     * @return the web service.
     */
    protected Service getService() {
        return service;
    }

    /**
     * Return the name of the web service.
     *
     * @return the name of the web service.
     */
    protected String getName() {
        return name;
    }

    /**
     * Return the type of web service.
     *
     * @return the type of web service.
     */
    protected WebServiceTypeEnum getWebServiceType() {
        return webServiceType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getUrl(final Call<Session> call) {
        return SalesforceWebServiceUtil.computeUrl(call.getValue(), getWebServiceType(), getName());
    }

    /**
     * Set the service, web service name, and web service type.
     *
     * @param service is the web service.
     * @param name is the name of the web service.
     * @param webServiceType is the type of web service.
     *
     * @throws IllegalArgumentException if any param is null or name is empty.
     */
    public SalesforceUrlDecorateCall(final Service service, final String name, final WebServiceTypeEnum webServiceType) {
        ParameterUtil.ensureParameter(service, "Cannot have a null service!");
        ParameterUtil.ensureParameter(name, "Cannot have a null/empty name!");
        ParameterUtil.ensureParameter(webServiceType, "Cannot have a null web service type!");

        this.service        = service;
        this.name           = name;
        this.webServiceType = webServiceType;
    }
}
