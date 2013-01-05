package org.solenopsis.lasius.wsimport.port.impl;

import java.util.logging.Logger;
import org.flossware.util.ParameterUtil;
import org.flossware.util.wsimport.port.PortDecorator;
import org.flossware.util.wsimport.port.impl.DefaultPortDecorator;
import org.solenopsis.lasius.wsimport.port.SalesforcePortDecorator;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * Abstract base class for Salesforce ports.
 *
 * @author sfloess
 *
 */
public abstract class AbstractPortMgr {
    /**
     * Our logger.
     */
    private final Logger logger;


    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected final Logger getLogger() {
        return logger;
    }


    /**
     * Default constructor.
     */
    protected AbstractPortMgr() {
        this.logger = Logger.getLogger(getClass().getName());
    }
}
