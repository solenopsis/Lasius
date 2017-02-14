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
 */
package org.solenopsis.lasius.wsimport.common.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import org.flossware.wsimport.service.WebService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.util.ExceptionUtil;
import org.solenopsis.lasius.wsdls.enterprise.GetUserInfoResult;
import org.solenopsis.lasius.wsdls.enterprise.Soap;
import org.solenopsis.lasius.wsdls.enterprise.UnexpectedErrorFault_Exception;
import org.solenopsis.lasius.wsimport.common.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.common.security.LoginResult;
import org.solenopsis.lasius.wsimport.common.session.Session;
import org.solenopsis.lasius.wsimport.common.session.mgr.SessionMgr;

/**
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class SalesforceWebServicePortInvokerTest {

    @Mock
    SessionMgr sessionMgr;

    @Mock
    Session session;

    @Mock
    LoginResult loginResult;

    @Mock
    Credentials credentials;

    Soap soap;

    @Mock
    Binding binding;

    String sessionId;
    String apiVersion;
    String url;

    @Mock
    WebService webService;

    @Mock
    GetUserInfoResult result;

    SalesforceWebServicePortInvoker portInvoker;

    Method method;

    Map<String, Object> requestContext;

    @Before
    public void init() throws UnexpectedErrorFault_Exception, NoSuchMethodException {
        Mockito.when(sessionMgr.getSession()).thenReturn(session);
        Mockito.when(session.getLoginResult()).thenReturn(loginResult);
        Mockito.when(loginResult.getCredentials()).thenReturn(credentials);

        sessionId = "SessionID " + System.currentTimeMillis();
        apiVersion = "" + System.currentTimeMillis();
        url = "http://foo.com/" + System.currentTimeMillis();

        Mockito.when(loginResult.getSessionId()).thenReturn(sessionId);
        Mockito.when(loginResult.getServerUrl()).thenReturn(url);

        Mockito.when(credentials.getApiVersion()).thenReturn(apiVersion);

        soap = Mockito.mock(Soap.class, Mockito.withSettings().extraInterfaces(BindingProvider.class));

        Mockito.when(soap.getUserInfo()).thenReturn(result);

        requestContext = new HashMap<>();
        requestContext.put("foo", "" + System.currentTimeMillis());

        Mockito.when(((BindingProvider) soap).getRequestContext()).thenReturn(requestContext);
        Mockito.when(((BindingProvider) soap).getBinding()).thenReturn(binding);

        Mockito.when(webService.getPort()).thenReturn(soap);
        Mockito.when(webService.getQName()).thenReturn(new QName("str1", "str2"));

        portInvoker = new SalesforceWebServicePortInvoker(sessionMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE);

        method = Soap.class.getMethod("getUserInfo", new Class[0]);
    }

    @Test
    public void test_handleException_relogin() throws Throwable {
        portInvoker.handleException(new InvocationTargetException(new RuntimeException(ExceptionUtil.INVALID_SESSION_ID)), method, session);

        Mockito.verify(sessionMgr, Mockito.times(1)).resetSession(session);
    }

    @Test
    public void test_handleException_retry() throws Throwable {
        portInvoker.handleException(new InvocationTargetException(new RuntimeException(ExceptionUtil.UNABLE_TO_LOCK_ROW)), method, session);

        Mockito.verify(sessionMgr, Mockito.times(0)).resetSession(session);
    }

    @Test(expected = InvocationTargetException.class)
    public void test_handleException_exception() throws Throwable {
        portInvoker.handleException(new InvocationTargetException(new IndexOutOfBoundsException()), method, session);

        Mockito.verify(sessionMgr, Mockito.times(0)).resetSession(session);
    }

    @Test
    public void test_relogin_InvalidSessionId() throws Throwable {
        Mockito.when(soap.getUserInfo()).thenThrow(new RuntimeException(ExceptionUtil.INVALID_SESSION_ID));

        try {
            portInvoker.invoke(webService, soap, method, new Object[0]);

            Assert.fail("Should not have reached this line");
        } catch (final IllegalStateException re) {

        }

        Mockito.verify(soap, Mockito.times(SalesforceWebServiceUtil.MAX_RETRIES)).getUserInfo();
        Mockito.verify(sessionMgr, Mockito.times(SalesforceWebServiceUtil.MAX_RETRIES)).resetSession(session);
    }

    @Test
    public void test_relogin_IOException() throws Throwable {
        Mockito.when(soap.getUserInfo()).thenThrow(new RuntimeException(new IOException()));

        try {
            portInvoker.invoke(webService, soap, method, new Object[0]);

            Assert.fail("Should not have reached this line");
        } catch (final IllegalStateException re) {

        }

        Mockito.verify(soap, Mockito.times(SalesforceWebServiceUtil.MAX_RETRIES)).getUserInfo();
        Mockito.verify(sessionMgr, Mockito.times(SalesforceWebServiceUtil.MAX_RETRIES)).resetSession(session);
    }

    @Test
    public void test_invoke_retry_ServerUnavailable() throws Throwable {
        Mockito.when(soap.getUserInfo()).thenThrow(new RuntimeException(ExceptionUtil.SERVER_UNAVAILABLE));

        try {
            portInvoker.invoke(webService, soap, method, new Object[0]);

            Assert.fail("Should not have reached this line");
        } catch (final IllegalStateException re) {

        }

        Mockito.verify(soap, Mockito.times(SalesforceWebServiceUtil.MAX_RETRIES)).getUserInfo();
        Mockito.verify(sessionMgr, Mockito.times(0)).resetSession(session);
    }

    @Test
    public void test_invoke_retry_UnableToLockRow() throws Throwable {
        Mockito.when(soap.getUserInfo()).thenThrow(new RuntimeException(ExceptionUtil.UNABLE_TO_LOCK_ROW));

        try {
            portInvoker.invoke(webService, soap, method, new Object[0]);

            Assert.fail("Should not have reached this line");
        } catch (final IllegalStateException ise) {

        }

        Mockito.verify(soap, Mockito.times(SalesforceWebServiceUtil.MAX_RETRIES)).getUserInfo();
        Mockito.verify(sessionMgr, Mockito.times(0)).resetSession(session);
    }

    @Test
    public void test_invoke() throws Throwable {
        Assert.assertSame("Should be the same result", result, portInvoker.invoke(webService, soap, method, new Object[0]));
    }
}
