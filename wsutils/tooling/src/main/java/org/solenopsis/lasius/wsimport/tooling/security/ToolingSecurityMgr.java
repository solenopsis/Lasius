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
package org.solenopsis.lasius.wsimport.tooling.security;

import org.flossware.wsimport.service.WebService;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsdls.tooling.SforceServicePortType;
import org.solenopsis.lasius.wsimport.common.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.common.security.AbstractSecurityMgr;
import org.solenopsis.lasius.wsimport.common.security.LoginResult;
import org.solenopsis.lasius.wsimport.common.util.SalesforceWebServiceUtil;

/**
 * Implementation using the tooling web service.
 *
 * @author Scot P. Floess
 */
public class ToolingSecurityMgr extends AbstractSecurityMgr<SforceServicePortType> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected WebServiceTypeEnum getWebServiceType() {
        return WebServiceTypeEnum.TOOLING_SERVICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected WebService<SforceServicePortType> getWebService() {
        return SalesforceWebServiceUtil.TOOLING_WEB_SERVICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LoginResult login(final SforceServicePortType port, final Credentials credentials) throws Exception {
        return new ToolingLoginResult(port.login(credentials.getUserName(), credentials.getSecurityPassword()), credentials, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void logout(final SforceServicePortType port) throws Exception {
        port.logout();
    }
}
