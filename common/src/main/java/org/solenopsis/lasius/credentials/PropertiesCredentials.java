package org.solenopsis.lasius.credentials;

import org.flossware.util.properties.PropertiesMgr;

/**
 *
 * Uses properties to populate the credentials.
 *
 * @author sfloess
 *
 */
public class PropertiesCredentials extends DefaultCredentials {

    /**
     * Sets the property manager and the property names for the data.
     *
     * @param propertiesMgr
     * @param urlProperty
     * @param userNameProperty
     * @param passwordProperty
     * @param tokenProperty
     * @param apiVersionProperty
     */
    public PropertiesCredentials(final PropertiesMgr propertiesMgr, final String urlProperty, final String userNameProperty, final String passwordProperty, final String tokenProperty, final String apiVersionProperty) {
        super(
                propertiesMgr.getProperties().getProperty(urlProperty),
                propertiesMgr.getProperties().getProperty(userNameProperty),
                propertiesMgr.getProperties().getProperty(passwordProperty),
                propertiesMgr.getProperties().getProperty(tokenProperty),
                propertiesMgr.getProperties().getProperty(apiVersionProperty)
        );
    }

    /**
     * Constructs credentials from propertiesMgr.
     *
     * @param propertiesMgr contains properties from which our credentials will be retrieved.
     */
    public PropertiesCredentials(final PropertiesMgr propertiesMgr) {
        this(
                propertiesMgr,
                CredentialsEnum.URL.getName(),
                CredentialsEnum.USER_NAME.getName(),
                CredentialsEnum.PASSWORD.getName(),
                CredentialsEnum.TOKEN.getName(),
                CredentialsEnum.API_VERSION.getName()
        );
    }
}
