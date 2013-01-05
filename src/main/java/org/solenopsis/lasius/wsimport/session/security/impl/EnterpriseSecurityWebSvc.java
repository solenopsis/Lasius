package org.solenopsis.lasius.wsimport.session.security.impl;

import java.util.logging.Level;
import org.flossware.util.ParameterUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.sforce.wsimport.enterprise.Soap;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.websvc.impl.EnterpriseWebSvc;

/**
 *
 * The enterprise.wsdl representation.
 *
 * @author sfloess
 *
 */
public final class EnterpriseSecurityWebSvc extends AbstractSecurityWebSvc<Soap> {
    /**
     * Default constructor.
     */
    public EnterpriseSecurityWebSvc(final EnterpriseWebSvc webSvc) {
        super(webSvc);
    }

    /**
     * Default constructor.
     */
    public EnterpriseSecurityWebSvc() {
        this(new EnterpriseWebSvc());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session login(final Credentials credentials) throws Exception {
        ParameterUtil.ensureParameter(credentials, "Cannot have null credentials!");

        getLogger().log(Level.INFO, "User [{0}] Password [{1}]", new Object[]{credentials.getUserName(), credentials.getSecurityPassword()});

        //return new EnterpriseSession(credentials, getPort(credentials).login(credentials.getUserName(), credentials.getSecurityPassword()));
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
