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

/**
 * Raised when a problem arises during a login.
 *
 * @author Scot P. Floess
 */
public class LoginException extends RuntimeException {

    /**
     * Default constructor.
     */
    public LoginException() {
    }

    /**
     * Sets the detail message.
     *
     * @param message the detail message.
     */
    public LoginException(final String message) {
        super(message);
    }

    /**
     * Sets the detail message and what caused self to be raised.
     *
     * @param message the detail message.
     * @param cause   the failure that caused self to be raised.
     */
    public LoginException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Sets the cause of self being raised.
     *
     * @param cause the failure that caused self to be raised.
     */
    public LoginException(final Throwable cause) {
        super(cause);
    }
}
