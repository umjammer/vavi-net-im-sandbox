/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.xmpp;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * Group.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040117 nsano initial version <br>
 */
public class Group extends Xmpp {

    /** */
    protected String getXmlName() {
        return "contact";
    }

    /** */
    Group(String name) {
        this.name = name;
    }

    /** */
    private String name;

    /** */
    public String getName() {
        return name;
    }

    /** */
    private Set<Contact> contacts = new HashSet<Contact>();

    /** */
    public void addContact(Contact contact) {
        contact.cgs.add(this);
        contacts.add(contact);
    }

    /** @see vavi.net.xmpp.Xmpp#handle(JabberConnection) */
    void handle(JabberConnection jc) throws IOException {
        // TODO Auto-generated method stub
    }
}

/* */
