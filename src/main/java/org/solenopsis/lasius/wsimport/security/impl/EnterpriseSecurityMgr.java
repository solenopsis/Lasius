package org.solenopsis.lasius.wsimport.security.impl;

import java.util.logging.Level;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * The enterprise.wsdl representation.
 *
 * @author sfloess
 *
 */
public final class EnterpriseSecurityMgr extends AbstractSecurityMgr {

    /**
     * Default constructor.
     */
    public EnterpriseSecurityMgr() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session login(final Credentials credentials) throws Exception {
        return new EnterpriseSession(credentials, preparePort(credentials, SalesforceWebServiceUtil.ENTERPRISE_SERVICE.getSoap(), WebServiceTypeEnum.ENTERPRISE_SERVICE).login(credentials.getUserName(), credentials.getSecurityPassword()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(final Session session) throws Exception {
        getLogger().log(Level.INFO, "Logging out for:  User [{0}] Password [{1}]", new Object[]{session.getCredentials().getUserName(), session.getCredentials().getSecurityPassword()});

        preparePort(session, SalesforceWebServiceUtil.ENTERPRISE_SERVICE, SalesforceWebServiceUtil.ENTERPRISE_SERVICE.getSoap(), WebServiceTypeEnum.ENTERPRISE_SERVICE).logout();
    }
}
