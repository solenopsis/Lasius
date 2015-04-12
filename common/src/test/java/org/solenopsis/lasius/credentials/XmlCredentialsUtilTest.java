package org.solenopsis.lasius.credentials;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the XML credentials util.
 *
 * @author Scot P. Floess
 */
public class XmlCredentialsUtilTest {

    @Test(expected = RuntimeException.class)
    public void test_createXmlConfiguration_nullString() {
        XmlCredentialsUtil.createXmlConfiguration((String) null);
    }

    @Test(expected = RuntimeException.class)
    public void test_createXmlConfiguration_emptyString() {
        XmlCredentialsUtil.createXmlConfiguration("");
    }

    @Test(expected = RuntimeException.class)
    public void test_createXmlConfiguration_nullFile() {
        XmlCredentialsUtil.createXmlConfiguration((File) null);
    }

    @Test(expected = RuntimeException.class)
    public void test_createXmlConfiguration_nullUrl() {
        XmlCredentialsUtil.createXmlConfiguration((URL) null);
    }

    @Test
    public void test_singleUserDefinedXmlConfiguration() {
        final Collection<Credentials> credsCollection = XmlCredentialsUtil.getCredentials(getClass().getClassLoader().getResource("singleUserDefinedXmlConfiguration.xml"), "org", "org.url", "user", "password", "token", "org.api");
        final Credentials[] creds = credsCollection.toArray(new Credentials[0]);

        Assert.assertEquals("Should have 1 element", 1, creds.length);
        Assert.assertEquals("Should be same url", "http://theurl", creds[0].getUrl());
        Assert.assertEquals("Should be same user name", "theuser", creds[0].getUserName());
        Assert.assertEquals("Should be same user name", "the password", creds[0].getPassword());
        Assert.assertEquals("Should be same user name", "the token", creds[0].getToken());
        Assert.assertEquals("Should be same user name", "30.0", creds[0].getApiVersion());
    }

    @Test
    public void test_multipleUserDefinedXmlConfiguration() {
        final Collection<Credentials> credsCollection = XmlCredentialsUtil.getCredentials(getClass().getClassLoader().getResource("mutlipleUserDefinedXmlConfiguration.xml"), "orgs.org", "orgs.url", "user", "password", "token", "orgs.api");
        final Credentials[] creds = credsCollection.toArray(new Credentials[0]);

        Assert.assertEquals("Should have 3 elements", 3, creds.length);

        Assert.assertEquals("Should be same url", "http://theurl", creds[0].getUrl());
        Assert.assertEquals("Should be same user name", "theuser1", creds[0].getUserName());
        Assert.assertEquals("Should be same user name", "the password1", creds[0].getPassword());
        Assert.assertEquals("Should be same user name", "the token1", creds[0].getToken());
        Assert.assertEquals("Should be same user name", "30.0", creds[0].getApiVersion());

        Assert.assertEquals("Should be same url", "http://theurl", creds[1].getUrl());
        Assert.assertEquals("Should be same user name", "theuser2", creds[1].getUserName());
        Assert.assertEquals("Should be same user name", "the password2", creds[1].getPassword());
        Assert.assertEquals("Should be same user name", "the token2", creds[1].getToken());
        Assert.assertEquals("Should be same user name", "30.0", creds[1].getApiVersion());

        Assert.assertEquals("Should be same url", "http://theurl", creds[2].getUrl());
        Assert.assertEquals("Should be same user name", "theuser3", creds[2].getUserName());
        Assert.assertEquals("Should be same user name", "the password3", creds[2].getPassword());
        Assert.assertEquals("Should be same user name", "the token3", creds[2].getToken());
        Assert.assertEquals("Should be same user name", "30.0", creds[2].getApiVersion());
    }

}
