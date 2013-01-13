package org.solenopsis.lasius.wsimport.query.impl;

import java.util.List;
import org.flossware.util.ParameterUtil;

/**
 *
 * Denotes an initial query being executed.
 *
 * @author sfloess
 *
 */
public class InitialQuery<V> {
    /**
     * When querying SFDC, this is our locator for calling queryMore()
     */
    private final String queryResultLocator;

    /**
     * Initially when executing the SFDC query, this is the first batch of
     * data returned.
     */
    private final List<V> initialList;

    /**
     * Set the query result locator and initial list.
     *
     * @param queryResultLocator the SFDC query result locator.
     * @param initialList contains first batch of data.
     *
     * @throws IllegalArgumentException if queryResultLocator is null/empty or intialList is null.
     */
    public InitialQuery(final String queryResultLocator, final List<V> initialList) {
        ParameterUtil.ensureParameter(queryResultLocator, "Cannot have an empty or null query result locator!");
        ParameterUtil.ensureParameter(initialList, 0, "Cannot have a null initial list!");

        this.queryResultLocator = queryResultLocator;
        this.initialList        = initialList;
    }

    /**
     * Return the query result locator.
     *
     * @return the query result locator.
     */
    public String getQueryResultLocator() {
        return queryResultLocator;
    }

    /**
     * Return the initial list.
     *
     * @return the initial list.
     */
    public List<V> getInitialList() {
        return initialList;
    }
}
