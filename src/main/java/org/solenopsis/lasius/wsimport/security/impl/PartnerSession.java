package org.solenopsis.lasius.wsimport.security.impl;

import org.flossware.util.NetUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.sforce.wsimport.partner.LoginResult;
import org.solenopsis.lasius.wsimport.session.impl.AbstractSession;

/**
 *
 * Denotes an SFDC session initiated using the partner.wsdl.  Also acts as an adapter
 * for the partner.wsdl's LoginResult.
 *
 * @author sfloess
 *
 */
public class PartnerSession extends AbstractSession<LoginResult> {
    /**
     * This constructor sets the credentials and login result.
     *
     * @param credentials the session's credentials.
     * @param loginResult the login result from SFDC.
     */
    public PartnerSession(final Credentials credentials, final LoginResult loginResult) {
        super(credentials, loginResult, NetUtil.computeUrlAsString(loginResult.getServerUrl()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMetadataServerUrl() {
        return getLoginResult().getMetadataServerUrl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPasswordExpired() {
        return getLoginResult().isPasswordExpired();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSandbox() {
        return getLoginResult().isSandbox();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSessionId() {
        return getLoginResult().getSessionId();
    }
}
