package org.solenopsis.lasius.wsimport.session.impl;

import org.flossware.util.NetUtil;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.wsdls.partner.LoginResult;

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
        super(credentials, loginResult, NetUtil.computeHostUrlAsString(loginResult.getServerUrl()));
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
