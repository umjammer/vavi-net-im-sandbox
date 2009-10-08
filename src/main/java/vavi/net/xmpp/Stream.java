/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.xmpp;

import java.io.IOException;
import java.io.Serializable;


/**
 * Stream.
 *
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	040125	nsano	initial version <br>
 */
public class Stream extends Xmpp implements Serializable {

    /** */
    protected String getXmlName() {
        return "stream:stream";
    }

    /** */
    private String to;

    /** */
    public void setTo(String to) {
        this.to = to;
    }

    /** */
    public String getTo() {
        return to;
    }

    /** */
    private String id;

    /** */
    public void setId(String id) {
        this.id = id;
    }

    /** */
    public String getId() {
        return id;
    }

    /** */
    private String sasl;

    /** */
    public void setSasl(String sasl) {
        this.sasl = sasl;
    }

    /** */
    public String getSasl() {
        return sasl;
    }

    /** TODO */
    private String xmlnsStream;

    /** */
    public void setXmlnsStream(String xmlnsStream) {
        this.xmlnsStream = xmlnsStream;
    }

    /** */
    public String getXmlnsStream() {
        return xmlnsStream;
    }

    /** @see vavi.net.xmpp.Xmpp#handle(JabberConnection) */
    void handle(JabberConnection jc) throws IOException {
        // TODO Auto-generated method stub
        
    }
}

/* */
