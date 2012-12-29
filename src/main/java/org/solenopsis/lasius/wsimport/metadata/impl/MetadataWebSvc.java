package org.solenopsis.lasius.wsimport.metadata.impl;

import java.net.URL;
import javax.xml.namespace.QName;
import org.flossware.util.wsimport.UrlUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.sforce.wsimport.metadata.MetadataPortType;
import org.solenopsis.lasius.sforce.wsimport.metadata.MetadataService;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.security.SecurityWebSvc;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Manages the metadata web service.
 *
 * @author sfloess
 *
 */
public class MetadataWebSvc {
    public static final String WSD_RESOURCE = "/wsdl/metadata.wsdl";
    public static final URL WSDL_URL = MetadataWebSvc.class.getResource(WSD_RESOURCE);
    public static final QName SERVICE_NAME = new MetadataService().getServiceName();
    public static final MetadataService WEB_SERVICE = new MetadataService(WSDL_URL, SERVICE_NAME);

    protected static MetadataService getWebService() {
        return WEB_SERVICE;
    }

    /**
     * Default constructor.
     */
    public MetadataWebSvc() {
    }

    public MetadataPortType getMetadataPortType(final Credentials credentials, final SecurityWebSvc securityWebSvc) throws Exception {
        final Session session = securityWebSvc.login(credentials);

        final MetadataPortType port = getWebService().getMetadata();

        UrlUtil.setUrl(port, session.getMetadataServerUrl() + "/" + WebServiceTypeEnum.METADATA_SERVICE.getUrlSuffix() + "/" + credentials.getApiVersion());

        SalesforceWebServiceUtil.setSessionId(port, getWebService(), session);

        return port;
    }
}
