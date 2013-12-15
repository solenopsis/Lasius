package org.solenopsis.lasius.wsimport.security.impl;

import org.solenopsis.lasius.wsimport.session.impl.EnterpriseSession;
import java.util.logging.Level;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

import org.solenopsis.wsdls.enterprise.SforceService;
import org.solenopsis.wsdls.enterprise.Soap;

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
        getLogger().log(Level.INFO, "Logging in for:  User [{0}] Password [{1}]", new Object[]{credentials.getUserName(), credentials.getSecurityPassword()});

        final Soap port = SalesforceWebServiceUtil.createEnterprisePort(credentials, SforceService.class);

        return new EnterpriseSession(credentials, port.login(credentials.getUserName(), credentials.getSecurityPassword()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(final Session session) throws Exception {
        getLogger().log(Level.INFO, "Logging out for:  User [{0}] Password [{1}]", new Object[]{session.getCredentials().getUserName(), session.getCredentials().getSecurityPassword()});

        final Soap port = SalesforceWebServiceUtil.createEnterprisePort(session, SforceService.class);

        port.logout();
    }
}
