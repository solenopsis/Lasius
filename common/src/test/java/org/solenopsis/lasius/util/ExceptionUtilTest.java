/*
 * Copyright (C) 2017 Scot P. Floess
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
 */package org.solenopsis.lasius.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the ExceptionUtil class...
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionUtilTest {

    @Test
    public void test_isExceptionMsg_String_String() {
        Assert.assertFalse("Should not be an exception msg", ExceptionUtil.isExceptionMsg("test a", (String) null));
        Assert.assertFalse("Should not be an exception msg", ExceptionUtil.isExceptionMsg("test b", "try 1"));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isExceptionMsg("test c", "try 2 test c lmn"));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isExceptionMsg("test d", "test d lmn"));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isExceptionMsg("test e", "try 2 test e"));
    }

    @Test
    public void test_isExceptionMsg_String_Throwable() {
        Assert.assertFalse("Should not be an exception msg", ExceptionUtil.isExceptionMsg("test a", new RuntimeException("hello world")));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isExceptionMsg("test c", new RuntimeException("try 2 test c lmn")));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isExceptionMsg("test d", new RuntimeException("test d lmn")));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isExceptionMsg("test e", new RuntimeException("try 2 test e")));

        Assert.assertFalse("Should not be an exception msg", ExceptionUtil.isExceptionMsg("test a", new InvocationTargetException(new RuntimeException("hello world"))));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isExceptionMsg("test c", new InvocationTargetException(new RuntimeException("try 2 test c lmn"))));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isExceptionMsg("test d", new InvocationTargetException(new RuntimeException("test d lmn"))));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isExceptionMsg("test e", new InvocationTargetException(new RuntimeException("try 2 test e"))));
    }

    @Test
    public void test_isInvalidSessionId_String() {
        Assert.assertFalse("Should not be an invalid session id", ExceptionUtil.isInvalidSessionId((String) null));
        Assert.assertFalse("Should not be an invalid session id", ExceptionUtil.isInvalidSessionId("test a"));

        Assert.assertTrue("Should be an invalid session id", ExceptionUtil.isInvalidSessionId(" foo " + ExceptionUtil.INVALID_SESSION_ID + " bar "));
        Assert.assertTrue("Should be an invalid session id", ExceptionUtil.isInvalidSessionId(" foo " + ExceptionUtil.INVALID_SESSION_ID));
        Assert.assertTrue("Should be an invalid session id", ExceptionUtil.isInvalidSessionId(ExceptionUtil.INVALID_SESSION_ID + "bar"));
        Assert.assertTrue("Should be an invalid session id", ExceptionUtil.isInvalidSessionId(ExceptionUtil.INVALID_SESSION_ID));
    }

    @Test
    public void test_isInvalidSessionId_Throwable() {
        Assert.assertFalse("Should not be an exception msg", ExceptionUtil.isInvalidSessionId(new RuntimeException("hello world")));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isInvalidSessionId(new RuntimeException(" foo " + ExceptionUtil.INVALID_SESSION_ID + " bar ")));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isInvalidSessionId(new RuntimeException(" foo " + ExceptionUtil.INVALID_SESSION_ID)));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isInvalidSessionId(new RuntimeException(ExceptionUtil.INVALID_SESSION_ID + "bar")));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isInvalidSessionId(new RuntimeException(ExceptionUtil.INVALID_SESSION_ID)));

        Assert.assertFalse("Should not be an exception msg", ExceptionUtil.isInvalidSessionId(new InvocationTargetException(new RuntimeException("hello world"))));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isInvalidSessionId(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.INVALID_SESSION_ID + " bar "))));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isInvalidSessionId(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.INVALID_SESSION_ID))));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isInvalidSessionId(new InvocationTargetException(new RuntimeException(ExceptionUtil.INVALID_SESSION_ID + "bar"))));
        Assert.assertTrue("Should be an exception msg", ExceptionUtil.isInvalidSessionId(new InvocationTargetException(new RuntimeException(ExceptionUtil.INVALID_SESSION_ID))));
    }

    @Test
    public void test_isServerUnavailable_String() {
        Assert.assertFalse("Should not be an invalid session id", ExceptionUtil.isServerUnavailable((String) null));
        Assert.assertFalse("Should not be an invalid session id", ExceptionUtil.isServerUnavailable("test a"));

        Assert.assertTrue("Should be an invalid session id", ExceptionUtil.isServerUnavailable(" foo " + ExceptionUtil.SERVER_UNAVAILABLE + " bar "));
        Assert.assertTrue("Should be an invalid session id", ExceptionUtil.isServerUnavailable(" foo " + ExceptionUtil.SERVER_UNAVAILABLE));
        Assert.assertTrue("Should be an invalid session id", ExceptionUtil.isServerUnavailable(ExceptionUtil.SERVER_UNAVAILABLE + "bar"));
        Assert.assertTrue("Should be an invalid session id", ExceptionUtil.isServerUnavailable(ExceptionUtil.SERVER_UNAVAILABLE));
    }

    @Test
    public void test_isServerUnavailable_Throwable() {
        Assert.assertFalse("Should not be a server unavailable exception", ExceptionUtil.isServerUnavailable(new RuntimeException("hello world")));
        Assert.assertTrue("Should be a server unavailable exception", ExceptionUtil.isServerUnavailable(new RuntimeException(" foo " + ExceptionUtil.SERVER_UNAVAILABLE + " bar ")));
        Assert.assertTrue("Should be an exception", ExceptionUtil.isServerUnavailable(new RuntimeException(" foo " + ExceptionUtil.SERVER_UNAVAILABLE)));
        Assert.assertTrue("Should be an exception", ExceptionUtil.isServerUnavailable(new RuntimeException(ExceptionUtil.SERVER_UNAVAILABLE + "bar")));
        Assert.assertTrue("Should be an exception", ExceptionUtil.isServerUnavailable(new RuntimeException(ExceptionUtil.SERVER_UNAVAILABLE)));

        Assert.assertFalse("Should not be a server unavailable exception", ExceptionUtil.isServerUnavailable(new InvocationTargetException(new RuntimeException("hello world"))));
        Assert.assertTrue("Should be a server unavailable exception", ExceptionUtil.isServerUnavailable(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.SERVER_UNAVAILABLE + " bar "))));
        Assert.assertTrue("Should be a server unavailable exception", ExceptionUtil.isServerUnavailable(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.SERVER_UNAVAILABLE))));
        Assert.assertTrue("Should be a server unavailable exception", ExceptionUtil.isServerUnavailable(new InvocationTargetException(new RuntimeException(ExceptionUtil.SERVER_UNAVAILABLE + "bar"))));
        Assert.assertTrue("Should be a server unavailable exception", ExceptionUtil.isServerUnavailable(new InvocationTargetException(new RuntimeException(ExceptionUtil.SERVER_UNAVAILABLE))));
    }

    @Test
    public void test_isUnableToLockRow_Throwable() {
        Assert.assertFalse("Should not be an unable to lock row exception", ExceptionUtil.isUnableToLockRow(new RuntimeException("hello world")));
        Assert.assertTrue("Should be an unable to lock row exception", ExceptionUtil.isUnableToLockRow(new RuntimeException(" foo " + ExceptionUtil.UNABLE_TO_LOCK_ROW + " bar ")));
        Assert.assertTrue("Should be an unable to lock row exception", ExceptionUtil.isUnableToLockRow(new RuntimeException(" foo " + ExceptionUtil.UNABLE_TO_LOCK_ROW)));
        Assert.assertTrue("Should be an unable to lock row exception", ExceptionUtil.isUnableToLockRow(new RuntimeException(ExceptionUtil.UNABLE_TO_LOCK_ROW + "bar")));
        Assert.assertTrue("Should be an unable to lock row exception", ExceptionUtil.isUnableToLockRow(new RuntimeException(ExceptionUtil.UNABLE_TO_LOCK_ROW)));

        Assert.assertFalse("Should not be an unable to lock row exception", ExceptionUtil.isUnableToLockRow(new InvocationTargetException(new RuntimeException("hello world"))));
        Assert.assertTrue("Should be an unable to lock row exception", ExceptionUtil.isUnableToLockRow(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.UNABLE_TO_LOCK_ROW + " bar "))));
        Assert.assertTrue("Should be an unable to lock row exception", ExceptionUtil.isUnableToLockRow(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.UNABLE_TO_LOCK_ROW))));
        Assert.assertTrue("Should be an unable to lock row exception", ExceptionUtil.isUnableToLockRow(new InvocationTargetException(new RuntimeException(ExceptionUtil.UNABLE_TO_LOCK_ROW + "bar"))));
        Assert.assertTrue("Should be an unable to lock row exception", ExceptionUtil.isUnableToLockRow(new InvocationTargetException(new RuntimeException(ExceptionUtil.UNABLE_TO_LOCK_ROW))));
    }

    @Test
    public void test_isReloginException_Throwable() {
        Assert.assertFalse("Should not be a relogin exception", ExceptionUtil.isReloginException(new RuntimeException("hello world")));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new RuntimeException(" foo " + ExceptionUtil.INVALID_SESSION_ID + " bar ")));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new RuntimeException(" foo " + ExceptionUtil.INVALID_SESSION_ID)));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new RuntimeException(ExceptionUtil.INVALID_SESSION_ID + "bar")));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new RuntimeException(ExceptionUtil.INVALID_SESSION_ID)));

        Assert.assertFalse("Should not be a relogin exception", ExceptionUtil.isReloginException(new InvocationTargetException(new RuntimeException("hello world"))));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.INVALID_SESSION_ID + " bar "))));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.INVALID_SESSION_ID))));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new InvocationTargetException(new RuntimeException(ExceptionUtil.INVALID_SESSION_ID + "bar"))));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new InvocationTargetException(new RuntimeException(ExceptionUtil.INVALID_SESSION_ID))));

        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new IOException("hello world")));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new IOException()));

        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new InvocationTargetException(new IOException("hello world"))));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtil.isReloginException(new InvocationTargetException(new IOException())));
    }

    @Test
    public void test_isRetryException_Throwable() {
        Assert.assertFalse("Should not be a retry exception", ExceptionUtil.isRetryException(new RuntimeException("hello world")));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new RuntimeException(" foo " + ExceptionUtil.SERVER_UNAVAILABLE + " bar ")));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new RuntimeException(" foo " + ExceptionUtil.SERVER_UNAVAILABLE)));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new RuntimeException(ExceptionUtil.SERVER_UNAVAILABLE + "bar")));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new RuntimeException(ExceptionUtil.SERVER_UNAVAILABLE)));

        Assert.assertFalse("Should not be a retry exception", ExceptionUtil.isRetryException(new InvocationTargetException(new RuntimeException("hello world"))));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.SERVER_UNAVAILABLE + " bar "))));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.SERVER_UNAVAILABLE))));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new InvocationTargetException(new RuntimeException(ExceptionUtil.SERVER_UNAVAILABLE + "bar"))));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new InvocationTargetException(new RuntimeException(ExceptionUtil.SERVER_UNAVAILABLE))));

        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new RuntimeException(" foo " + ExceptionUtil.UNABLE_TO_LOCK_ROW + " bar ")));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new RuntimeException(" foo " + ExceptionUtil.UNABLE_TO_LOCK_ROW)));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new RuntimeException(ExceptionUtil.UNABLE_TO_LOCK_ROW + "bar")));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new RuntimeException(ExceptionUtil.UNABLE_TO_LOCK_ROW)));

        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.UNABLE_TO_LOCK_ROW + " bar "))));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new InvocationTargetException(new RuntimeException(" foo " + ExceptionUtil.UNABLE_TO_LOCK_ROW))));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new InvocationTargetException(new RuntimeException(ExceptionUtil.UNABLE_TO_LOCK_ROW + "bar"))));
        Assert.assertTrue("Should be a retry exception", ExceptionUtil.isRetryException(new InvocationTargetException(new RuntimeException(ExceptionUtil.UNABLE_TO_LOCK_ROW))));
    }
}
