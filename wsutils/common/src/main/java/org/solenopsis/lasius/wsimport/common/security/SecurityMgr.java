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

import org.solenopsis.lasius.credentials.Credentials;

/**
 * Represents a way to login and out of SFDC. Can be used to abstract out the
 * enterprise, partner and tooling web services.
 *
 * @author Scot P. Floess
 */
public interface SecurityMgr {

    /**
     * Request a new login.
     *
     * @param credentials are the credentials to use to login.
     *
     * @return a login result from SFDC.
     */
    LoginResult login(Credentials credentials);

    /**
     * Force a logout.
     *
     * @param loginResult in which we desire a logout.
     */
    void logout(LoginResult loginResult);
}
