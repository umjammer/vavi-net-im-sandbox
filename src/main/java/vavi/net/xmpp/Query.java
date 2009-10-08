/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.xmpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Query.
 *
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	040125	nsano	initial version <br>
 */
public class Query extends Xmpp {

    /** */
    static class Feature extends Xmpp {

        /** */
        protected String getXmlName() {
            return "feature";
        }

        /** */
        private String var;

        /** */
        public void setVar(String var) {
            this.var = var;
        }

        /** @see vavi.net.xmpp.Xmpp#handle(JabberConnection) */
        void handle(JabberConnection jc) throws IOException {
            // TODO Auto-generated method stub
            
        }
    }

    /** */
    protected String getXmlName() {
        return "query";
    }

    /** */
    private Feature feature;

    /** */
    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    /** */
    private String name;

    /** */
    public void setName(String name) {
        this.name = name;
    }

    /** */
    private String version;

    /** */
    public void setVersion(String version) {
        this.version = version;
    }

    /** */
    private String userName;

    /** */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** */
    private String password;

    /** */
    public void setPassword(String password) {
        this.password = password;
    }

    /** */
    public String getPassword() {
        return password;
    }

    /** */
    private String resource;

    /** */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /** */
    private String digest;

    /** */
    public void setDigest(String digest) {
        this.digest = digest;
    }

    /** */
    private String email;

    /** */
    public void setEmail(String email) {
        this.email = email;
    }

    /** */
    private String os;

    /** */
    public void setOs(String os) {
        this.os = os;
    }

    //----

    /** */
    List<Item> items = new ArrayList<Item>();

    /** */
    public void addItem(Item item) {
        items.add(item);
    }

    /** */
    public List<Item> getItems() {
        return items;
    }

    /** @see vavi.net.xmpp.Xmpp#handle(JabberConnection) */
    void handle(JabberConnection jc) throws IOException {
        // TODO Auto-generated method stub
        
    }
}

/* */
