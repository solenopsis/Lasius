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
package org.solenopsis.lasius.wsimport.common.security;

import java.util.logging.Level;
import org.flossware.common.AbstractCommonBase;
import org.flossware.common.IntegrityUtil;
import org.flossware.wsimport.service.WebService;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.wsimport.common.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.common.util.SalesforceWebServiceUtil;

/**
 * Represents a way to login and out of SFDC. Can be used to abstract out the enterprise, partner and tooling web
 * services.
 *
 * @author Scot P. Floess
 *
 * @param <P> th e port type
 */
public abstract class AbstractSecurityMgr<P> extends AbstractCommonBase implements SecurityMgr {

    /**
     * Default constructor.
     */
    protected AbstractSecurityMgr() {
    }

    /**
     * Return our web service type.
     *
     * @return the web service type.
     */
    protected abstract WebServiceTypeEnum getWebServiceType();

    /**
     * Return the web service class to use when creating a login/logout port.
     *
     * @return the web service class to use when creating a login/logout port.
     */
    protected abstract WebService<P> getWebService();

    /**
     * Will request child classes to perform a login.
     *
     * @param port        the port to use upon login.
     * @param credentials the credentials used for login.
     *
     * @return a login result.
     */
    protected abstract LoginResult login(final P port, Credentials credentials);

    /**
     * Will request child classes to perform a logout.
     *
     * @param port the port to use when logging out.
     */
    protected abstract void logout(P port);

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginResult login(final Credentials credentials) {
        IntegrityUtil.ensure(credentials, "Cannot have null credentials on login!");

        try {
            final P port = getWebService().getPort();
            SalesforceWebServiceUtil.setVerbatimUrl(port, credentials, getWebServiceType(), credentials.getApiVersion());

            return login(port, credentials);
        } catch (final RuntimeException runtimeException) {
            getLogger().log(Level.WARNING, "Trouble logging in", runtimeException);

            throw runtimeException;
        } catch (final Exception exception) {
            getLogger().log(Level.WARNING, "Trouble logging in", exception);

            throw new LoginException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(final LoginResult loginResult) {
        IntegrityUtil.ensure(loginResult, "Cannot have null login result on logout!");

        try {
            logout(SalesforceWebServiceUtil.createPort(loginResult, getWebServiceType(), getWebService()));
        } catch (final RuntimeException runtimeException) {
            getLogger().log(Level.WARNING, "Trouble logging out", runtimeException);

            throw runtimeException;
        } catch (final Exception exception) {
            getLogger().log(Level.WARNING, "Trouble logging out", exception);

            throw new LogoutException(exception);
        }
    }
}
