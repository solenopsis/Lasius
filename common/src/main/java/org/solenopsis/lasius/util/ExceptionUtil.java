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
package org.solenopsis.lasius.util;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 *
 * @author Scot P. Floess
 */
public class ExceptionUtil {

    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(ExceptionUtil.class.getName());

    /**
     * Denotes an invalid session id.
     */
    public static final String INVALID_SESSION_ID = "INVALID_SESSION_ID";

    /**
     * Denotes server is unavailable.
     */
    public static final String SERVER_UNAVAILABLE = "SERVER_UNAVAILABLE";

    /**
     * Denotes server is unavailable.
     */
    public static final String SERVICE_UNAVAILABLE = "Service Unavailable";

    /**
     * Denotes unable to lock a row.
     */
    public static final String UNABLE_TO_LOCK_ROW = "UNABLE_TO_LOCK_ROW";

    /**
     * Return the logger.
     */
    protected static Logger getLogger() {
        return logger;
    }

    /**
     * Return true if message contains invalid session id.
     *
     * @param toCompare is the
     * @param message   is the message to examine for being an invalid session
     *                  id.
     */
    public static boolean isExceptionMsg(final String toCompare, final String message) {
        return (null == message ? false : message.contains(toCompare));
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    public static boolean isExceptionMsg(final String toCompare, final Throwable failure) {
        if (failure instanceof InvocationTargetException) {
            return isExceptionMsg(toCompare, ((InvocationTargetException) failure).getTargetException().getMessage());
        }

        return isExceptionMsg(toCompare, failure.getMessage());
    }

    /**
     * Return true if message contains invalid session id.
     *
     * @param message is the message to examine for being an invalid session id.
     */
    public static boolean isInvalidSessionId(final String message) {
        return isExceptionMsg(INVALID_SESSION_ID, message);
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    public static boolean isInvalidSessionId(final Throwable failure) {
        return isExceptionMsg(INVALID_SESSION_ID, failure);
    }

    /**
     * Return true if message contains server unavailable.
     *
     * @param message is the message to examine for being server unavailable.
     */
    public static boolean isServerUnavailable(final String message) {
        return isExceptionMsg(SERVER_UNAVAILABLE, message);
    }

    /**
     * Return true if we have server unavailable or false if not.
     *
     * @param failure is the failure to examine for server unavailable.
     */
    public static boolean isServerUnavailable(final Throwable failure) {
        return isExceptionMsg(SERVER_UNAVAILABLE, failure);
    }

    /**
     * Return true if we have service is unavailable or false if not.
     *
     * @param failure is the failure to examine for service unavailable.
     */
    public static boolean isServiceUnavailable(final Throwable failure) {
        return isExceptionMsg(SERVICE_UNAVAILABLE, failure);
    }

    /**
     * Return true if we are unable to lock a row in SFDC.
     *
     * @param failure is the failure to examine for unable to lock row.
     */
    public static boolean isUnableToLockRow(final Throwable failure) {
        return isExceptionMsg(UNABLE_TO_LOCK_ROW, failure);
    }

    /**
     * Returns true if the failure represents one where relogin should occur.
     *
     * @param failure the exception to examine if relogin is necessary.
     *
     * @return true if relogin is necessary.
     */
    public static boolean isReloginException(final Throwable failure) {
        return isInvalidSessionId(failure) || org.flossware.util.ExceptionUtil.containsIOException(failure);
    }

    /**
     * Returns true if the failure represents one where a retry should occur.
     *
     * @param failure the exception to examine if retry is necessary.
     *
     * @return true if retry is necessary.
     */
    public static boolean isRetryException(final Throwable failure) {
        return isServerUnavailable(failure) || isUnableToLockRow(failure) || isServiceUnavailable(failure);
    }

    /**
     * Not allowed.
     */
    private ExceptionUtil() {
    }
}
