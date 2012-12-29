package org.solenopsis.lasius.wsimport.session.security.impl;

import java.net.URL;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import org.flossware.util.ParameterUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.sforce.wsimport.enterprise.SforceService;
import org.solenopsis.lasius.sforce.wsimport.enterprise.Soap;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;

/**
 *
 * The enterprise.wsdl representation.
 *
 * @author sfloess
 *
 */
public final class EnterpriseSecurityWebSvc extends AbstractSecurityWebSvc<SforceService, Soap> {
    private static final String WSD_RESOURCE = "/wsdl/enterprise.wsdl";
    private static final URL WSDL_URL = EnterpriseSecurityWebSvc.class.getResource(WSD_RESOURCE);
    private static final QName SERVICE_NAME = new SforceService().getServiceName();
    private static final SforceService WEB_SERVICE = new SforceService(WSDL_URL, SERVICE_NAME);

    /**
     * {@inheritDoc}
     */
    @Override
    protected Soap createPort() {
        return getWebService().getSoap();
    }

    /**
     * Default constructor.
     */
    public EnterpriseSecurityWebSvc() {
        super(WEB_SERVICE, WebServiceTypeEnum.ENTERPRISE_SERVICE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session login(final Credentials credentials) throws Exception {
        ParameterUtil.ensureParameter(credentials, "Cannot have null credentials!");

        getLogger().log(Level.INFO, "User [{0}] Password [{1}]", new Object[]{credentials.getUserName(), credentials.getSecurityPassword()});

        return new EnterpriseSession(credentials, getPort(credentials).login(credentials.getUserName(), credentials.getSecurityPassword()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(final Session session) throws Exception {
        getLogger().log(Level.INFO, "Logging out for:  User [{0}] Password [{1}]", new Object[]{session.getCredentials().getUserName(), session.getCredentials().getSecurityPassword()});

        getPort(session).logout();
    }
}
