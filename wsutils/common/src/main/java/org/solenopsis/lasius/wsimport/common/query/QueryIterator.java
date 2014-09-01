package org.solenopsis.lasius.wsimport.common.query;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.flossware.common.IntegrityUtil;

/**
 *
 * Iterates over a query result.
 *
 * @author sfloess
 *
 */
public class QueryIterator<V> implements Iterator<V> {

    /**
     * Our logger.
     */
    private final Logger logger;

    /**
     * Can perform Salesforce queries.
     */
    private final AbstractQueryMgr queryMgr;

    /**
     * When querying, this is the locator for the next batch.
     */
    private final String queryResultLocator;

    /**
     * The results we will process.
     */
    private Iterator<V> resultIterator;

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected final Logger getLogger() {
        return logger;
    }

    /**
     * Return our query manager.
     *
     * @return our query manager.
     */
    protected AbstractQueryMgr getQueryMgr() {
        return queryMgr;
    }

    /**
     * Return our query result locator.
     *
     * @return the query result locator.
     */
    protected String getQueryResultLocator() {
        return queryResultLocator;
    }

    /**
     * Set the result list.
     *
     * @param resultList is the new result list.
     */
    protected void setResultIterator(final Iterator<V> resultIterator) {
        this.resultIterator = resultIterator;
    }

    /**
     * Get the result list.
     *
     * @return the result iterator.
     */
    protected Iterator<V> getResultIterator() {
        return resultIterator;
    }

    /**
     * Sets the query result.
     *
     * @param queryMgr the query result locator.
     *
     * @throws IllegalArgumentException if queryMgr is null or soql is
     *                                  empty/null.
     */
    public QueryIterator(final AbstractQueryMgr queryMgr, final String soql) {
        IntegrityUtil.ensure(queryMgr, "Query manager cannot be null!");
        IntegrityUtil.ensure(soql, "SOQL cannot be null or empty!");

        final InitialQuery initialQuery = queryMgr.querySalesforce(soql);

        this.logger = Logger.getLogger(getClass().getName());
        this.queryMgr = queryMgr;
        this.queryResultLocator = initialQuery.getQueryResultLocator();
        this.resultIterator = initialQuery.getInitialList().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        if (!getResultIterator().hasNext()) {
            try {
                setResultIterator(getQueryMgr().getNext(getQueryResultLocator()).iterator());
            } catch (final Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        return getResultIterator().hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V next() {
        return getResultIterator().next();
    }

    /**
     * Not supported!
     */
    @Override
    public void remove() {
        getLogger().log(Level.WARNING, "Remove is not supported!");
    }
}
