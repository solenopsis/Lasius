package org.solenopsis.lasius.wsimport.security.impl;

import java.util.logging.Level;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.impl.ToolingSession;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;


/**
 *
 * Manages the Partner WSDL
 *
 * @author sfloess
 *
 */
public final class ToolingSecurityMgr extends AbstractSecurityMgr  {
    /**
     * Default constructor.
     */
    public ToolingSecurityMgr() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session login(final Credentials credentials) throws Exception {
        getLogger().log(Level.INFO, "Logging in for:  User [{0}] Password [{1}]", new Object[]{credentials.getUserName(), credentials.getSecurityPassword()});

        return new ToolingSession(credentials, SalesforceWebServiceUtil.toolingLogin(credentials));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(final Session session) throws Exception {
        getLogger().log(Level.INFO, "Logging out for:  User [{0}] Password [{1}]", new Object[]{session.getCredentials().getUserName(), session.getCredentials().getSecurityPassword()});

        SalesforceWebServiceUtil.partnerLogout(session);
    }
}
