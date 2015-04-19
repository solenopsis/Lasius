/*
 * Copyright (C) 2014 Scot P. Floess
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.solenopsis.lasius.wsimport.partner.security;

import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsdls.partner.LoginResult;
import org.solenopsis.lasius.wsimport.common.security.AbstractLoginResult;

/**
 * Enterprise web service login result.
 *
 * @author Scot P. Floess
 */
class PartnerLoginResult extends AbstractLoginResult<LoginResult, PartnerSecurityMgr> implements org.solenopsis.lasius.wsimport.common.security.LoginResult<PartnerSecurityMgr> {

    /**
     * Constructor sets the login result and security manager who constructed self.
     *
     * @param loginResult result of login.
     * @param credentials the credentials usef ro login.
     * @param securityMgr security manager who created self.
     *
     * @throws IllegalArgumentException if loginResult or securityManager are null.
     */
    PartnerLoginResult(final LoginResult loginResult, final Credentials credentials, final PartnerSecurityMgr securityMgr) {
        super(loginResult, loginResult.getServerUrl(), credentials, securityMgr);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return getLoginResult().getUserId();
    }
}
