package org.solenopsis.lasius.wsimport.websvc.impl;

import java.net.URL;
import javax.xml.namespace.QName;
import org.solenopsis.lasius.sforce.wsimport.partner.SforceService;
import org.solenopsis.lasius.sforce.wsimport.partner.Soap;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.port.SalesforcePortDecorator;
import org.solenopsis.lasius.wsimport.port.SalesforcePortFactory;

/**
 *
 * Manages the Partner WSDL.
 *
 * @author sfloess
 *
 */
public final class PartnerWebSvc extends AbstractWebSvc<Soap> {
    private static final String DEFAULT_WSDL_RESOURCE = "/wsdl/partner.wsdl";
    private static final QName SERVICE_NAME = new SforceService().getServiceName();

    protected PartnerWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final SforceService webService, final String apiVersion) {
        super(portDecorator, portFactory, WebServiceTypeEnum.PARTNER_SERVICE, webService, Soap.class, apiVersion);
    }

    public PartnerWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final URL wsdlUrl, final String apiVersion) {
        this(portDecorator, portFactory, new SforceService(wsdlUrl, SERVICE_NAME), apiVersion);
    }

    public PartnerWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final String wsdlResource, final String apiVersion) {
        this(portDecorator, portFactory, SforceService.class.getResource(wsdlResource), apiVersion);
    }

    public PartnerWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final String apiVersion) {
        this(portDecorator, portFactory, DEFAULT_WSDL_RESOURCE, apiVersion);
    }

    public PartnerWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory) {
        this(portDecorator, portFactory, DEFAULT_API_VERSION);
    }

    public PartnerWebSvc(final URL wsdlUrl, final String apiVersion) {
        this(DEFAULT_PORT_DECORATOR, DEFAULT_PORT_FACTORY, wsdlUrl, apiVersion);
    }

    public PartnerWebSvc(final String wsdlResource, final String apiVersion) {
        this(DEFAULT_PORT_DECORATOR, DEFAULT_PORT_FACTORY, wsdlResource, apiVersion);
    }

    public PartnerWebSvc(final String apiVersion) {
        this(DEFAULT_PORT_DECORATOR, DEFAULT_PORT_FACTORY, apiVersion);
    }

    /**
     * Default constructor.
     */
    public PartnerWebSvc() {
        this(DEFAULT_API_VERSION);
    }
}
