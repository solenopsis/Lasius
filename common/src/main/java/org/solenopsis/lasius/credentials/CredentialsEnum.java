package org.solenopsis.lasius.credentials;

/**
 * For properties the property name for each credential. For XML, the name of an element.
 *
 * @author Scot P. Floess
 */
public enum CredentialsEnum {

    URL("url"),
    USER_NAME("username"),
    PASSWORD("password"),
    TOKEN("token"),
    API_VERSION("apiVersion");

    private final String name;

    private CredentialsEnum(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
