package org.solenopsis.lasius.wsimport.websvc.impl;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * Just a convenience class for using services.  Really doesn't add much value but its an option to use none-the-less.
 *
 * @author sfloess
 *
 */
public class DefaultService extends Service {
    public DefaultService(String wsdlResource, final QName serviceName) {
        super(DefaultService.class.getResource(wsdlResource), serviceName);
    }

    public DefaultService(String wsdlResource, final Service service) {
        this(wsdlResource, service.getServiceName());
    }
}
