package org.solenopsis.lasius.credentials.impl;

import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.credentials.CredentialsUtil;

/**
 *
 * Uses properties to populate the credentials.
 *
 * @author sfloess
 *
 */
abstract class AbstractCredentials implements Credentials {        
    protected AbstractCredentials() {        
    }
    
    @Override
    public final String getSecurityPassword() {
        return CredentialsUtil.computeSecurityPassword(getPassword(), getToken());
    }  
}
