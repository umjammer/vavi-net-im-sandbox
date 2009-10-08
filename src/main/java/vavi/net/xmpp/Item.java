/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.xmpp;

import java.io.IOException;


/**
 * Item.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040125 nsano initial version <br>
 */
public class Item extends Xmpp {

    /** */
    protected String getXmlName() {
        return "item";
    }

    /** */
    private String subscription;

    /** */
    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    /** */
    public String getSubscription() {
        return subscription;
    }

    /** */
    private String jid;

    /** */
    public void setJid(String jid) {
        this.jid = jid;
    }

    /** */
    public String getJid() {
        return jid;
    }

    /** */
    private String name;

    /** */
    public void setName(String name) {
        this.name = name;
    }

    /** */
    public String getName() {
        return name;
    }

    /** */
    private String group;

    /** */
    public void setGroup(String group) {
        this.group = group;
    }

    /** */
    public String getGroup() {
        return group;
    }

    /** @see vavi.net.xmpp.Xmpp#handle(JabberConnection) */
    void handle(JabberConnection jc) throws IOException {
        // TODO Auto-generated method stub
        
    }
}

/* */
