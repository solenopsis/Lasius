package org.solenopsis.lasius.ws.standard;

import org.solenopsis.lasius.sforce.soap.partner.SforceService;
import org.solenopsis.lasius.sforce.soap.partner.Soap;
import org.solenopsis.lasius.ws.AbstractWebSvc;
import org.solenopsis.lasius.ws.SecurityWebSvc;
import org.solenopsis.lasius.ws.StandardWebServiceWsdlEnum;
import org.solenopsis.lasius.ws.WebServiceTypeEnum;

/**
 *
 * Manages the Partner WSDL
 *
 * @author sfloess
 *
 */
public final class PartnerWebSvc extends AbstractWebSvc<SforceService, Soap> {
    public PartnerWebSvc(final SecurityWebSvc securitySvc) throws Exception {
        super (
            new SforceService(StandardWebServiceWsdlEnum.PARTNER_SERVICE.getWsdlResource(), StandardWebServiceWsdlEnum.PARTNER_SERVICE.getQName()),
            securitySvc.getCredentials().getApiVersion(),
            WebServiceTypeEnum.PARTNER_SERVICE,
            securitySvc
        );
    }
    
    @Override
    protected  Soap createPort() {
        return getService().getSoap();
    }
}
