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

import org.flossware.wsimport.service.WebService;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsdls.partner.Soap;
import org.solenopsis.lasius.wsimport.common.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.common.security.AbstractSecurityMgr;
import org.solenopsis.lasius.wsimport.common.security.LoginException;
import org.solenopsis.lasius.wsimport.common.security.LoginResult;
import org.solenopsis.lasius.wsimport.common.security.LogoutException;
import org.solenopsis.lasius.wsimport.common.util.SalesforceWebServiceUtil;

/**
 * Implementation using the partner web service.
 *
 * @author Scot P. Floess
 */
public class PartnerSecurityMgr extends AbstractSecurityMgr<Soap> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected WebServiceTypeEnum getWebServiceType() {
        return WebServiceTypeEnum.PARTNER_SERVICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected WebService<Soap> getWebService() {
        return SalesforceWebServiceUtil.PARTNER_WEB_SERVICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LoginResult login(final Soap port, final Credentials credentials) {
        try {
            return new PartnerLoginResult(port.login(credentials.getUserName(), credentials.getSecurityPassword()), credentials, this);
        } catch (final RuntimeException runtimeException) {
            throw runtimeException;
        } catch (final Throwable throwable) {
            throw new LoginException(throwable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void logout(final Soap port) {
        try {
            port.logout();
        } catch (final RuntimeException runtimeException) {
            throw runtimeException;
        } catch (final Throwable throwable) {
            throw new LogoutException(throwable);
        }
    }
}
