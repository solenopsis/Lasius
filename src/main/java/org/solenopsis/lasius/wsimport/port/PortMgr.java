package org.solenopsis.lasius.wsimport.port;

import javax.xml.ws.Service;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;

/**
 *
 * This interface defines the API for decorating SFDC web service ports.  For example,
 * for session limits per login, retries, etc.
 *
 * @author sfloess
 *
 */
public interface PortMgr {
    /**
     * Default retries.
     */
    public static final int DEFAULT_MAX_RETRIES = 4;

    /**
     * Generate a port for use.
     *
     * @param <P> the type of port we will decorate.
     *
     * @param webServiceType is the type of web service being decorated.
     * @param service is the web service to use.
     * @param portType the class of the port type.
     * @param name the name of the web service.
     * @param maxRetries the number of times to retry calls to SFDC before failing.
     *
     * @return a port with decorated functionality like limited concurrent access to SFDC.
     *
     * @throws Exception if any problems arise generating our return value.
     */
    <P> P createPort(WebServiceTypeEnum webServiceType, Service service, Class<P> portType, String name, int maxRetries) throws Exception;

    /**
     * Generate a port for use.
     *
     * @param <P> the type of port we will decorate.
     *
     * @param webServiceType is the type of web service being decorated.
     * @param service is the web service to use.
     * @param portType the class of the port type.
     * @param name the name of the web service.
     *
     * @return a port with decorated functionality like limited concurrent access to SFDC.
     *
     * @throws Exception if any problems arise generating our return value.
     */
    <P> P createPort(WebServiceTypeEnum webServiceType, Service service, Class<P> portType, String name) throws Exception;
}
