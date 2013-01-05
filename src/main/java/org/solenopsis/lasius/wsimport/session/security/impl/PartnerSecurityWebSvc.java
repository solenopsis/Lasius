package org.solenopsis.lasius.wsimport.session.security.impl;

import java.util.logging.Level;
import org.flossware.util.ParameterUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.sforce.wsimport.partner.Soap;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.websvc.impl.PartnerWebSvc;

/**
 *
 * Manages the Partner WSDL
 *
 * @author sfloess
 *
 */
public final class PartnerSecurityWebSvc extends AbstractSecurityWebSvc<Soap>  {
    /**
     * Default constructor.
     */
    public PartnerSecurityWebSvc(final PartnerWebSvc webSvc) {
        super(webSvc);
    }

    /**
     * Default constructor.
     */
    public PartnerSecurityWebSvc() {
        this(new PartnerWebSvc());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session login(final Credentials credentials) throws Exception {
        ParameterUtil.ensureParameter(credentials, "Cannot have null credentials!");

        getLogger().log(Level.INFO, "User [{0}] Password [{1}]", new Object[]{credentials.getUserName(), credentials.getSecurityPassword()});

        return new PartnerSession(credentials, getPort(credentials).login(credentials.getUserName(), credentials.getSecurityPassword()));
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
