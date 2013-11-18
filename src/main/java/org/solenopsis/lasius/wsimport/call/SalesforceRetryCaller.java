package org.solenopsis.lasius.wsimport.call;

import javax.xml.ws.Service;
import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.retry.impl.AbstractRetryCaller;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;

/**
 *
 * Will perform calls on a Salesforce port.
 *
 * @author sfloess
 *
 */
public class SalesforceRetryCaller extends AbstractRetryCaller {
    /**
     * {@inheritDoc}
     */
    @Override
    protected int getMaxRetries() {
        return SalesforceWebServiceUtil.MAX_RETRIES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isToRetryCall(Call call, Exception failure) throws Exception {
        return SalesforceWebServiceUtil.isReloginException(failure);
    }

    /**
     * Default constructor.
     */
    public SalesforceRetryCaller(final SessionMgr sessionMgr, final WebServiceTypeEnum webServiceType, final Service service, final Class<?> portType, final String name) {
        super(new SalesforceCaller(sessionMgr, webServiceType, service, portType, name));
    }
}
