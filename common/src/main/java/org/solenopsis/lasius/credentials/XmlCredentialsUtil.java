package org.solenopsis.lasius.credentials;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.flossware.common.IntegrityUtil;

/**
 *
 * Retrieves credentials from an XML source.
 *
 * The rootElement params below represent the child elements of the root node. For example, if you have a document the
 * resembles:
 *
 * root/parent/child/username1...child/username2
 *
 * The the root element is parent.child - note the omission of the "root" element.
 *
 * @author sfloess
 *
 */
public class XmlCredentialsUtil {

    /**
     * Creates an XMLConfigurtion for fileName.
     *
     * @param fileName the file name to use.
     *
     * @return an XMLConfiguration for fileName.
     *
     * @throws RuntimeException if a failure arises creating the XML configuration.
     */
    public static XMLConfiguration createXmlConfiguration(final String fileName) {
        try {
            return new XMLConfiguration(IntegrityUtil.ensure(fileName, "Must provide a file name"));
        } catch (final ConfigurationException configurationException) {
            throw new RuntimeException("Trouble using file name [" + fileName + "]", configurationException);
        }
    }

    /**
     * Creates an XMLConfigurtion for fileName.
     *
     * @param file the file to use.
     *
     * @return an XMLConfiguration for file.
     *
     * @throws RuntimeException if a failure arises creating the XML configuration.
     */
    public static XMLConfiguration createXmlConfiguration(final File file) {
        try {
            return new XMLConfiguration(IntegrityUtil.ensure(file, "Must provide a file"));
        } catch (final ConfigurationException configurationException) {
            throw new RuntimeException("Trouble using file [" + file + "]", configurationException);
        }
    }

    /**
     * Creates an XMLConfigurtion for url.
     *
     * @param url the url to use.
     *
     * @return an XMLConfiguration for url.
     *
     * @throws RuntimeException if a failure arises creating the XML configuration.
     */
    public static XMLConfiguration createXmlConfiguration(final URL url) {
        try {
            return new XMLConfiguration(IntegrityUtil.ensure(url, "Must provide a URL"));
        } catch (final ConfigurationException configurationException) {
            throw new RuntimeException("Trouble using URL [" + url + "]", configurationException);
        }
    }

    /**
     * Converts hierarchicalConfiguration to a credentials object. The element params are the names of the XML elements
     * that contain data.
     */
    public static Credentials convertToCredentials(final HierarchicalConfiguration hierarchicalConfiguration, final String urlElement, final String userNameElement, final String passwordElement, final String tokenElement, final String apiVersionElement) {
        IntegrityUtil.ensure(urlElement, "Must provide a url element!");
        IntegrityUtil.ensure(userNameElement, "Must provide a user name element!");
        IntegrityUtil.ensure(passwordElement, "Must provide a password element!");
        IntegrityUtil.ensure(tokenElement, "Must provide a token element!");
        IntegrityUtil.ensure(apiVersionElement, "Must provide an api version element!");

        return new DefaultCredentials(
                hierarchicalConfiguration.getString(urlElement),
                hierarchicalConfiguration.getString(userNameElement),
                hierarchicalConfiguration.getString(passwordElement),
                hierarchicalConfiguration.getString(tokenElement),
                hierarchicalConfiguration.getString(apiVersionElement)
        );
    }

    /**
     * Returns a collection of credentials from the xmlConfiguration.
     *
     * @param xmlConfiguration  contains the credentials.
     *
     * @param rootElement       the path to the root element containing all the credentials.
     *
     * @param urlElement        the name of the XML element for URL under rootElement.
     * @param userNameElement   the name of the XML element for user name under rootElement.
     * @param passwordElement   the name of the XML element for the password under rootElement.
     * @param tokenElement      the name of the XML element for the token under rootElement.
     * @param apiVersionElement the name of the XML element for the api version under rootElement.
     *
     * @return
     */
    public static Collection<Credentials> getCredentials(final XMLConfiguration xmlConfiguration, final String rootElement, final String urlElement, final String userNameElement, final String passwordElement, final String tokenElement, final String apiVersionElement) {
        IntegrityUtil.ensure(xmlConfiguration, "Must provide an XMLConfiguration!");
        IntegrityUtil.ensure(rootElement, "Must provide a root element!");
        IntegrityUtil.ensure(urlElement, "Must provide a url element!");
        IntegrityUtil.ensure(userNameElement, "Must provide a user name element!");
        IntegrityUtil.ensure(passwordElement, "Must provide a password element!");
        IntegrityUtil.ensure(tokenElement, "Must provide a token element!");
        IntegrityUtil.ensure(apiVersionElement, "Must provide an api version element!");

        final List<HierarchicalConfiguration> configurationList = xmlConfiguration.configurationsAt(rootElement);
        IntegrityUtil.ensure(configurationList, "No configuration found using [" + rootElement + "]");

        final List<Credentials> retVal = new ArrayList(configurationList.size());

        for (final HierarchicalConfiguration hierarchicalConfiguration : configurationList) {
            retVal.add(convertToCredentials(hierarchicalConfiguration, urlElement, userNameElement, passwordElement, tokenElement, apiVersionElement));
        }

        return retVal;
    }

    public static Collection<Credentials> getCredentials(final XMLConfiguration xmlConfiguration, final String rootElement) {
        return getCredentials(xmlConfiguration, rootElement, CredentialsEnum.URL.getName(), CredentialsEnum.USER_NAME.getName(), CredentialsEnum.PASSWORD.getName(), CredentialsEnum.TOKEN.getName(), CredentialsEnum.API_VERSION.getName());
    }

    public static Collection<Credentials> getCredentials(final String fileName, final String rootElement, final String urlElement, final String userNameElement, final String passwordElement, final String tokenElement, final String apiVersionElement) {
        return getCredentials(createXmlConfiguration(fileName), rootElement, urlElement, userNameElement, passwordElement, tokenElement, apiVersionElement);
    }

    public static Collection<Credentials> getCredentials(final String fileName, final String rootElement) {
        return getCredentials(createXmlConfiguration(fileName), rootElement);
    }

    public static Collection<Credentials> getCredentials(final File file, final String rootElement, final String urlElement, final String userNameElement, final String passwordElement, final String tokenElement, final String apiVersionElement) {
        return getCredentials(createXmlConfiguration(file), rootElement, urlElement, userNameElement, passwordElement, tokenElement, apiVersionElement);
    }

    public static Collection<Credentials> getCredentials(final File file, final String rootElement) {
        return getCredentials(createXmlConfiguration(file), rootElement);
    }

    public static Collection<Credentials> getCredentials(final URL url, final String rootElement, final String urlElement, final String userNameElement, final String passwordElement, final String tokenElement, final String apiVersionElement) {
        return getCredentials(createXmlConfiguration(url), rootElement, urlElement, userNameElement, passwordElement, tokenElement, apiVersionElement);
    }

    public static Collection<Credentials> getCredentials(final URL url, final String rootElement) {
        return getCredentials(createXmlConfiguration(url), rootElement);
    }
}
