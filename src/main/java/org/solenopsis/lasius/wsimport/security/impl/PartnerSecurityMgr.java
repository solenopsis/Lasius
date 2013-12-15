package org.solenopsis.lasius.wsimport.security.impl;

import java.util.logging.Level;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.impl.PartnerSession;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

import org.solenopsis.wsdls.partner.SforceService;
import org.solenopsis.wsdls.partner.Soap;

/**
 *
 * Manages the Partner WSDL
 *
 * @author sfloess
 *
 */
public final class PartnerSecurityMgr extends AbstractSecurityMgr  {
    /**
     * Default constructor.
     */
    public PartnerSecurityMgr() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session login(final Credentials credentials) throws Exception {
        getLogger().log(Level.INFO, "Logging in for:  User [{0}] Password [{1}]", new Object[]{credentials.getUserName(), credentials.getSecurityPassword()});

        final Soap port = SalesforceWebServiceUtil.createPartnerPort(credentials, SforceService.class);

        return new PartnerSession(credentials, port.login(credentials.getUserName(), credentials.getSecurityPassword()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(final Session session) throws Exception {
        getLogger().log(Level.INFO, "Logging out for:  User [{0}] Password [{1}]", new Object[]{session.getCredentials().getUserName(), session.getCredentials().getSecurityPassword()});

        final Soap port = SalesforceWebServiceUtil.createPartnerPort(session, SforceService.class);

        port.logout();
    }
}
