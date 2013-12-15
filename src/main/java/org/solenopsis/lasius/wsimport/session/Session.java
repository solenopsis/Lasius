package org.solenopsis.lasius.wsimport.session;

import java.util.concurrent.Semaphore;
import org.solenopsis.lasius.credentials.Credentials;

/**
 *
 * Denotes an SFDC session.
 *
 * @author sfloess
 *
 */
public interface Session {
    /**
     * Default number of concurrent calls to SFDC.
     */
    public static final int DEFAULT_CONCURRENT_ACCESS = 10;

    public void lock() throws Exception;

    public void unlock() throws Exception;

    public int getRemainingLocks() throws Exception;

    /**
     * Return a session id.  This is not the SFDC session id.
     *
     * @return a session id.
     */
    public Object getId();

    /**
     * Return the credentials.
     *
     * @return the session's credentials.
     */
    public Credentials getCredentials();

    /**
     * Return the URL of the server being used for this session.
     *
     * @return the URL of the server being used for this session.
     */
    public String getServerUrl();

    /**
     * Return the URL for the metadata server.
     *
     * @return return the url for the metadata server.
     */
    public String getMetadataServerUrl();

    /**
     * Return true if the password is expired or false if not.
     *
     * @return true if the password is expired.
     */
    public boolean isPasswordExpired();

    /**
     * Return true if this session is for a sandbox.
     *
     * @return true if this session is for a sandbox or false if not.
     */
    public boolean isSandbox();

    /**
     * Return the SFDC session id.
     *
     * @return the SFDC session id.
     */
    public String getSessionId();
}
