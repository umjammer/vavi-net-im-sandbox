/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.xmpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;


/**
 * Contact.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040117 nsano initial version <br>
 */
public class Contact extends Xmpp {

    /** */
    protected String getXmlName() {
        return "contact";
    }

    List<Group> cgs = new ArrayList<Group>();
    private String entry;
    public void setEntry(String entry) {
        this.entry = entry;
    }
    private String subscription;
    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private ImageIcon foto;
    public ImageIcon getFoto() {
        return foto;
    }
    public void setFoto(ImageIcon foto) {
        this.foto = foto;
    }
    private String jid;
    public String getJid() {
        return jid;
    }
    public void setJid(String jid) {
        this.jid = jid;
    }
    private String show;
    public void setShow(String show) {
        this.show = show;
    }
    public String getShow() {
        return show;
    }
    private String status;
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    private String desc;
    private String phone;
    private String bday;
    private String hp;

    private String fn;
    private String url;

    private String type;
    private String photo;
    private byte[] binVal;

    /** */
    public Contact(Presence presence) {
    }

    /** */
    public Contact(Query query) {
    }

    /** */
    public Contact(VCard vCard) {

        this.xmlns = vCard.getXmlns();

        this.fn = vCard.getFn();
        this.desc = vCard.getDesc();
        this.bday = vCard.getBday();
        this.url = vCard.getUrl();
        List<VCard.Tel> tels = vCard.getTels();
        for (int i = 0; i < tels.size(); i++) {
            VCard.Tel t = tels.get(i);
            if (t.getVoice() != null) {
                if (t.getNumber() != null)
                    this.phone = t.getNumber();
            }
        }
        this.photo = vCard.getPhoto();
        if (photo != null) {
            this.type = vCard.getType();
            this.binVal = vCard.getBinVal();
            if (type != null & binVal != null) {
                byte[] buf = new Base64().decode(binVal);
                ImageIcon foto = new ImageIcon(buf);
                this.foto = foto;
            }
        }
    }

    /** @see vavi.net.xmpp.Xmpp#handle(JabberConnection) */
    void handle(JabberConnection jc) throws IOException {
        // TODO Auto-generated method stub
    }
}

/* */

