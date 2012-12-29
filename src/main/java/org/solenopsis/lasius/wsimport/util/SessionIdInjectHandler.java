package org.solenopsis.lasius.wsimport.util;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.solenopsis.lasius.wsimport.metadata.impl.MetadataWebSvc;

/**
 *
 * This class will inject the session id into the metadata api header.
 *
 * @author sfloess
 *
 */
public final class SessionIdInjectHandler implements SOAPHandler<SOAPMessageContext> {
    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(SessionIdInjectHandler.class.getName());

    private final String qname;

    /**
     * Holds our session id.
     */
    private final String sessionId;

    /**
     * Return the logger.
     */
    protected static Logger getLogger() {
        return logger;
    }

    protected String getQname() {
        return qname;
    }

    /**
     * Return the session id.
     */
    protected String getSessionId() {
        return sessionId;
    }


    /**
     * Default constructor.
     */
    public SessionIdInjectHandler(final String qname, final String sessionId) {
        this.qname     = qname;
        this.sessionId = sessionId;
    }

    public SessionIdInjectHandler(final QName qname, final String sessionId) {
        this(qname.getNamespaceURI(), sessionId);
    }

    /**
     * Default constructor.
     */
    public SessionIdInjectHandler(final Service service, final String sessionId) {
        this(service.getServiceName(), sessionId);
    }

    /**
     *@{inheritDoc}
     */
    @Override
    public boolean handleMessage(final SOAPMessageContext msgCcontext) {
        final Boolean isRequest = (Boolean) msgCcontext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        //if this is a request, true for outbound messages, false for inbound
        if(isRequest) {
            try {
                SOAPMessage soapMsg = msgCcontext.getMessage();
                SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
                SOAPHeader soapHeader = soapEnv.getHeader();

                //if no header, add one
                if (soapHeader == null){
                    soapHeader = soapEnv.addHeader();
                }

                QName sessionHeaderQname = new QName(getQname(), "SessionHeader");
                SOAPElement sessionHeaderElement = soapHeader.addChildElement(sessionHeaderQname);

                //SOAPElement sessionHeaderElement = soapHeader.addChildElement(MetadataWebSvc.SERVICE_NAME);

                SOAPElement sessionIdElement = sessionHeaderElement.addChildElement("sessionId");

                sessionIdElement.addTextNode(getSessionId());

                soapMsg.saveChanges();

                if (getLogger().isLoggable(Level.INFO)) {
                    getLogger().log(Level.INFO, "Set session id in header to [{0}]", sessionId);
                }
            }

            catch(final SOAPException soapException) {
                getLogger().log(Level.SEVERE, "Problem setting session id [" + getSessionId() + "]", soapException);
            }
        }

        return true;
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        getLogger().log(Level.SEVERE, context.getMessage().getContentDescription());

        return true;
    }

    @Override
    public void close(MessageContext context) {
    }
}
