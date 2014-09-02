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

    public enum PropertyNameEnum {

        URL("url"),
        USER_NAME("username"),
        PASSWORD("password"),
        TOKEN("token"),
        API_VERSION("apiVersion");

        private final String name;

        private PropertyNameEnum(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    };

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
     * @param propertiesMgr contains properties from which our credentials will
     *                      be retrieved.
     */
    public PropertiesCredentials(final PropertiesMgr propertiesMgr) {
        this(
                propertiesMgr,
                PropertyNameEnum.URL.getName(),
                PropertyNameEnum.USER_NAME.getName(),
                PropertyNameEnum.PASSWORD.getName(),
                PropertyNameEnum.TOKEN.getName(),
                PropertyNameEnum.API_VERSION.getName()
        );
    }
}
