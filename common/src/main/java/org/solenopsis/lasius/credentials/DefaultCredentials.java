package org.solenopsis.lasius.credentials;

/**
 *
 * Default implementation of Credentials.
 *
 * @author sfloess
 *
 */
public class DefaultCredentials extends AbstractCredentials {

    private final String url;
    private final String userName;
    private final String password;
    private final String token;
    private final String apiVersion;

    public DefaultCredentials(final String url, final String userName, final String password, final String token, final String apiVersion) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.token = token;
        this.apiVersion = apiVersion;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getApiVersion() {
        return apiVersion;
    }
}
