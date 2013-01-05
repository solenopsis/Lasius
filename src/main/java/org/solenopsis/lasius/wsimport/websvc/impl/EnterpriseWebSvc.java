package org.solenopsis.lasius.wsimport.websvc.impl;

import java.net.URL;
import javax.xml.namespace.QName;
import org.solenopsis.lasius.sforce.wsimport.enterprise.SforceService;
import org.solenopsis.lasius.sforce.wsimport.enterprise.Soap;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.port.SalesforcePortDecorator;
import org.solenopsis.lasius.wsimport.port.SalesforcePortFactory;


/**
 *
 * The enterprise.wsdl representation.
 *
 * @author sfloess
 *
 */
public class EnterpriseWebSvc extends AbstractWebSvc<Soap> {
    private static final String DEFAULT_WSDL_RESOURCE = "/wsdl/enterprise.wsdl";
    private static final QName SERVICE_NAME = new SforceService().getServiceName();

    protected EnterpriseWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final SforceService webService, final String apiVersion) {
        super(portDecorator, portFactory, WebServiceTypeEnum.ENTERPRISE_SERVICE, webService, Soap.class, apiVersion);
    }

    public EnterpriseWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final URL wsdlUrl, final String apiVersion) {
        this(portDecorator, portFactory, new SforceService(wsdlUrl, SERVICE_NAME), apiVersion);
    }

    public EnterpriseWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final String wsdlResource, final String apiVersion) {
        this(portDecorator, portFactory, SforceService.class.getResource(wsdlResource), apiVersion);
    }

    public EnterpriseWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final String apiVersion) {
        this(portDecorator, portFactory, DEFAULT_WSDL_RESOURCE, apiVersion);
    }

    public EnterpriseWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory) {
        this(portDecorator, portFactory, DEFAULT_API_VERSION);
    }

    public EnterpriseWebSvc(final URL wsdlUrl, final String apiVersion) {
        this(DEFAULT_PORT_DECORATOR, DEFAULT_PORT_FACTORY, wsdlUrl, apiVersion);
    }

    public EnterpriseWebSvc(final String wsdlResource, final String apiVersion) {
        this(DEFAULT_PORT_DECORATOR, DEFAULT_PORT_FACTORY, wsdlResource, apiVersion);
    }

    public EnterpriseWebSvc(final String apiVersion) {
        this(DEFAULT_PORT_DECORATOR, DEFAULT_PORT_FACTORY, apiVersion);
    }

    /**
     * Default constructor.
     */
    public EnterpriseWebSvc() {
        this(DEFAULT_API_VERSION);
    }
}
