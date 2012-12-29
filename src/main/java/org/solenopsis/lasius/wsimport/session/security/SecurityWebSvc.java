package org.solenopsis.lasius.wsimport.session.security;

import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.credentials.Credentials;

/**
 *
 * Denotes a service that can be used to login to SFDC.  Defined in a neutral
 * way considering both an enterprise.wsdl and partner.wsdl.
 *
 * @author sfloess
 *
 */
public interface SecurityWebSvc {
    /**
     * Request a new login.
     *
     * @param credentials are the credentials to use to login.
     *
     * @return a session that represents a session to SFDC.
     *
     * @throws Exception if any problems arise logging in.
     */
    public Session login(Credentials credentials) throws Exception;

    /**
     * Force a logout.
     *
     * @param session is the session in which we desire a logout.
     *
     * @throws Exception if any problems arise logging out.
     */
    public void logout(Session session) throws Exception;

}
