package org.solenopsis.lasius.wsimport.common.query;

import java.util.Iterator;

/**
 *
 * Defines the API to query from SFDC.
 *
 * @author sfloess
 *
 */
public interface QueryMgr<V> {

    /**
     * Execute <code>soql</code> and return an iterator.
     *
     * @param soql the SOQL statement when querying.
     *
     * @return an iterator of objects from query.
     */
    Iterator<V> query(String soql);
}
