package org.solenopsis.lasius.wsimport.websvc.impl;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;

/**
 *
 * This may simplify using the framework for web services.  It's likely overkill and not needed.
 *
 * @author sfloess
 *
 */
public class DefaultWebSvc<P> extends AbstractWebSvc<P> {
    public DefaultWebSvc(final String wsdlResource, final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name) {
        super(webServiceType, new DefaultService(wsdlResource, service), portType, name);
    }

    public DefaultWebSvc(final String wsdlResource, final WebServiceTypeEnum webServiceType, final QName serviceName, final Class<P> portType, final String name) {
        super(webServiceType, new DefaultService(wsdlResource, serviceName), portType, name);
    }
}
