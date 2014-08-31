package org.solenopsis.lasius.credentials;

import org.solenopsis.lasius.util.CredentialsUtil;
import org.flossware.common.Stringifiable;

/**
 *
 * Uses properties to populate the credentials.
 *
 * @author sfloess
 *
 */
public abstract class AbstractCredentials implements Credentials, Stringifiable {

    /**
     * Default constructor.
     */
    protected AbstractCredentials() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Credentials)) {
            return false;
        }

        return getUserName().equals(((Credentials) object).getUserName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getUserName().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSecurityPassword() {
        return CredentialsUtil.computeSecurityPassword(getPassword(), getToken());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toString(final StringBuilder sb, final String prefix) {
        sb.append(prefix).append("apiVersion       [").append(getApiVersion()).append("]\n");
        sb.append(prefix).append("password         [").append(getPassword()).append("]\n");
        sb.append(prefix).append("securityPassword [").append(getSecurityPassword()).append("]\n");
        sb.append(prefix).append("token            [").append(getToken()).append("]\n");
        sb.append(prefix).append("url              [").append(getUrl()).append("]\n");
        sb.append(prefix).append("userName         [").append(getUserName()).append("]");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toString(final StringBuilder sb) {
        toString(sb, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(final String prefix) {
        final StringBuilder sb = new StringBuilder();

        toString(sb, prefix);

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString("");
    }
}
