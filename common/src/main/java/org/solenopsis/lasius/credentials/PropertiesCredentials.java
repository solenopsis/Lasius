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

    /**
     * Constructs credentials from propertiesMgr.
     *
     * @param propertiesMgr contains properties from which our credentials will
     *                      be retrieved.
     */
    public PropertiesCredentials(PropertiesMgr propertiesMgr) {
        super(
                propertiesMgr.getProperties().getProperty(PropertyNameEnum.URL.getName()),
                propertiesMgr.getProperties().getProperty(PropertyNameEnum.USER_NAME.getName()),
                propertiesMgr.getProperties().getProperty(PropertyNameEnum.PASSWORD.getName()),
                propertiesMgr.getProperties().getProperty(PropertyNameEnum.TOKEN.getName()),
                propertiesMgr.getProperties().getProperty(PropertyNameEnum.API_VERSION.getName())
        );
    }
}
