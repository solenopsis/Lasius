
package org.solenopsis.lasius.wsimport.port.call.impl;

import org.flossware.util.ParameterUtil;
import org.flossware.util.reflect.Call;
import org.flossware.util.reflect.decorate.call.impl.AbstractDecorateCall;
import org.solenopsis.lasius.wsimport.session.Session;
import org.solenopsis.lasius.wsimport.session.mgr.SessionMgr;

/**
 *
 * Decorates calls by creating a session.
 *
 * @author sfloess
 *
 */
public class CreateSessionDecorateCall extends AbstractDecorateCall<Session> {
    /**
     * The session manager.
     */
    private final SessionMgr sessionMgr;

    /**
     * Return the session manager.
     *
     * @return the session manager.
     */
    protected SessionMgr geSessionMgr() {
        return sessionMgr;
    }

    /**
     * Default constructor
     */
    public CreateSessionDecorateCall(final SessionMgr sessionMgr) {
        ParameterUtil.ensureParameter(sessionMgr, "Cannot have a null session mgr!");

        this.sessionMgr = sessionMgr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Call<Session> decorate(final Call<Session> call) throws Exception {
        call.setValue(geSessionMgr().getSession());

        return call;
    }
}
