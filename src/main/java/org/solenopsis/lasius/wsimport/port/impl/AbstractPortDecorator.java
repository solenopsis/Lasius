package org.solenopsis.lasius.wsimport.port.impl;

import org.flossware.util.ParameterUtil;
import org.flossware.util.wsimport.port.PortDecorator;
import org.flossware.util.wsimport.port.impl.DefaultPortDecorator;
import org.solenopsis.lasius.wsimport.port.SalesforcePortDecorator;

/**
 *
 * Abstract base class for port decorators.
 *
 * @author sfloess
 *
 */
public abstract class AbstractPortDecorator extends AbstractPortMgr implements SalesforcePortDecorator {
    /**
     * The object that can perform decoration.
     */
    private final PortDecorator decorator;

    /**
     * Return our decorator.
     *
     * @return the decorator.
     */
    protected PortDecorator getDecorator() {
        return decorator;
    }

    /**
     * This constructor sets the session manager and decorator.
     *
     * @param decorator is the object for whom decoration will be created.
     *
     * @throws IllegalArgumentException if decorator is null.
     */
    protected AbstractPortDecorator(final PortDecorator decorator) {
        ParameterUtil.ensureParameter(decorator, "Decorator cannot be null!");

        this.decorator = decorator;
    }

    /**
     * This constructor sets the session manager.  It will use a default decorator.
     */
    protected AbstractPortDecorator() {
        this(new DefaultPortDecorator());
    }

}
