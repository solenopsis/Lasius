package org.solenopsis.lasius.wsimport.query;

import java.util.Iterator;
import org.solenopsis.lasius.sforce.wsimport.enterprise.QueryResult;
import org.solenopsis.lasius.sforce.wsimport.enterprise.SObject;

/**
 *
 * This class queries cases.
 *
 * @author sfloess
 *
 */
public abstract class AbstractQuery<V> implements QueryIterator<V> {
    
    private QueryResult queryResult;
    
    private Iterator<SObject> itr;
    
    protected AbstractQuery() {
    }
    
    protected void clearQueryResult() {
        queryResult = null;
    }
    
    protected abstract String getSoql() throws Exception;

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean hasNext() throws Exception {
        /*
        if (queryResult == null) {
            queryResult = EnterpriseWebService.getInstance().getPort().query(getSoql());
            itr = queryResult.getRecords().iterator();
        }
        
        if (!itr.hasNext()) {
            queryResult = EnterpriseWebService.getInstance().getPort().queryMore(queryResult.getQueryLocator());            
            itr = queryResult.getRecords().iterator();
        }
        
        return itr.hasNext();
        * 
        */
        
        return true;
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public V next() {
        SObject s = itr.next();
        
        return (V) s;
    }
}
