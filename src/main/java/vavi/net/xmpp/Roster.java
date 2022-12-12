/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.xmpp;

import java.util.HashMap;
import java.util.Map;


/**
 * Roster.
 *
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	040117	nsano	initial version <br>
 */
public class Roster {

    /** */
    private Map<String, Group> groups = new HashMap<>();

    /** */
    public Group getGroup(String name) {
        return groups.get(name);
    }

    /** */
    public Roster(Query query) {
        for (int i = 0; i < query.getItems().size(); i++) {
            Item item = query.getItems().get(i);
            String jid = item.getJid();
            String name = item.getName();
            String subs = item.getSubscription();
            Contact c = new Contact(query);	// TODO
            c.setJid(jid);
            if (name != null) {
                c.setEntry(name);
            }
            c.setSubscription(subs);
            boolean groupfound = false;
            String g = item.getGroup();
            if (g != null) {
                //first add the group to the contact
                c.cgs.add(new Group(g));
                //second add the contact to the group
                getGroup(g).addContact(c);
                groupfound = true;
            }
            if (!groupfound) {
                //this is called if no group is found
                //first add the group to the contact
                groups.put("groupless", new Group("groupless"));
                //second add the contact to the group
                getGroup("groupless").addContact(c);
            }
            // adding the contact to the e4 roster list
            // TODO move the roster-vector into the roster class
         }
    }
}

/* */
