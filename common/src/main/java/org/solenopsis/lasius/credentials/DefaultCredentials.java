package org.solenopsis.lasius.credentials;

import org.flossware.common.IntegrityUtil;

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
        this.url = IntegrityUtil.ensure(url, "Must have a URL");
        this.userName = IntegrityUtil.ensure(userName, "Must have a username");
        this.password = IntegrityUtil.ensure(password, "Must have a password");
        this.token = IntegrityUtil.ensure(token, "Must have a token");
        this.apiVersion = IntegrityUtil.ensure(apiVersion, "Must have a api version");
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
