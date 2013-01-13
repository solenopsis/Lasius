package org.solenopsis.lasius.wsimport.query.impl;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.solenopsis.lasius.wsimport.query.QueryMgr;

/**
 *
 * Defines the API to query from SFDC.
 *
 * @author sfloess
 *
 */
public abstract class AbstractQueryMgr<V> implements QueryMgr<V> {
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
     * Perform our initial query.
     *
     * @param soql is the soql statement to query for.
     *
     * @return an initial query with the appropriate data.
     *
     * @throws Exception if there are any problems querying Salesforce.
     */
    protected abstract InitialQuery<V> querySalesforce(final String soql) throws Exception;

    /**
     * Return the next batch of data from SFDC.
     *
     * @param queryResultLocator used when calling queryMore().
     *
     * @return the next batch of data.
     *
     * @throws Exception if there is any problem querying Salesforce.
     */
    protected abstract List<V> getNext(String queryResultLocator) throws Exception;

    /**
     * This constructor sets the port.
     */
    public AbstractQueryMgr() {
        this.logger = Logger.getLogger(getClass().getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<V> query(final String soql) throws Exception {
        return new QueryIterator(this, soql);
    }
}
