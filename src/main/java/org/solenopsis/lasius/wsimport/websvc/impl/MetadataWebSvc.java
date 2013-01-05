package org.solenopsis.lasius.wsimport.websvc.impl;

import java.net.URL;
import javax.xml.namespace.QName;
import org.solenopsis.lasius.sforce.wsimport.metadata.MetadataPortType;
import org.solenopsis.lasius.sforce.wsimport.metadata.MetadataService;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.port.SalesforcePortDecorator;
import org.solenopsis.lasius.wsimport.port.SalesforcePortFactory;

/**
 *
 * Manages the metadata web service.
 *
 * @author sfloess
 *
 */
public class MetadataWebSvc extends AbstractWebSvc<MetadataPortType> {
    public static final String DEFAULT_WSDL_RESOURCE = "/wsdl/metadata.wsdl";
    public static final QName SERVICE_NAME = new MetadataService().getServiceName();

    protected MetadataWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final MetadataService webService, final String apiVersion) {
        super(portDecorator, portFactory, WebServiceTypeEnum.METADATA_SERVICE, webService, MetadataPortType.class, apiVersion);
    }

    public MetadataWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final URL wsdlUrl, final String apiVersion) {
        this(portDecorator, portFactory, new MetadataService(wsdlUrl, SERVICE_NAME), apiVersion);
    }

    public MetadataWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final String wsdlResource, final String apiVersion) {
        this(portDecorator, portFactory, MetadataWebSvc.class.getResource(wsdlResource), apiVersion);
    }

    public MetadataWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory, final String apiVersion) {
        this(portDecorator, portFactory, DEFAULT_WSDL_RESOURCE, apiVersion);
    }

    public MetadataWebSvc(final SalesforcePortDecorator portDecorator, final SalesforcePortFactory portFactory) {
        this(portDecorator, portFactory, DEFAULT_API_VERSION);
    }

    public MetadataWebSvc(final URL wsdlUrl, final String apiVersion) {
        this(DEFAULT_PORT_DECORATOR, DEFAULT_PORT_FACTORY, wsdlUrl, apiVersion);
    }

    public MetadataWebSvc(final String wsdlResource, final String apiVersion) {
        this(DEFAULT_PORT_DECORATOR, DEFAULT_PORT_FACTORY, wsdlResource, apiVersion);
    }

    public MetadataWebSvc(final String apiVersion) {
        this(DEFAULT_PORT_DECORATOR, DEFAULT_PORT_FACTORY, apiVersion);
    }

    /**
     * Default constructor.
     */
    public MetadataWebSvc() {
        this(DEFAULT_API_VERSION);
    }
}
