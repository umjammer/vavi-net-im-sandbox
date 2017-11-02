/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.xmpp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;


/**
 * VCard.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040117 nsano initial version <br>
 */
public class VCard extends Xmpp {

    /** */
    protected String getXmlName() {
        return "vCard";
    }

    /** */
    private byte[] binVal;

    /** */
    public void setBinVal(byte[] binVal) {
        this.binVal = binVal;
    }

    /** */
    public byte[] getBinVal() {
        return binVal;
    }

    /** */
    private ImageIcon foto;

    /** */
    public void setFoto(ImageIcon foto) {
        this.foto = foto;
    }

    /** */
    public ImageIcon getFoto() {
        return foto;
    }

    /** */
    private String fotoString;

    /** */
    public void setFotoString(String fotoString) {
        this.fotoString = fotoString;
    }

    /** */
    public String getFotoString() {
        return fotoString;
    }

    /** */
    public VCard(VCard vCard) {
    }

    /** */
    public VCard() {
        try {
            File f = new File(fotoString);
            InputStream in = new FileInputStream(f);
            int length = (int) f.length();
            binVal = new byte[length];
            int l = 0;
            while (l < length) {
                in.read(binVal, l, length - l);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /** */
    private String surName;

    /** */
    public void setSurName(String surName) {
        this.surName = surName;
    }

    /** */
    public String getSurName() {
        return surName;
    }

    /** */
    private String firstName;

    /** */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** */
    public String getFirstName() {
        return firstName;
    }

    /** */
    private String url;

    /** */
    public void setUrl(String url) {
        this.url = url;
    }

    /** */
    public String getUrl() {
        return url;
    }

    /** */
    private String bday;

    /** */
    public void setBday(String bday) {
        this.bday = bday;
    }

    /** */
    public String getBday() {
        return bday;
    }

    /** */
    private String orgName;

    /** */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /** */
    public String getOrgName() {
        return orgName;
    }

    /** */
    private String orgUnit;

    /** */
    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    /** */
    public String getOrgUnit() {
        return orgUnit;
    }

    /** */
    private String title;

    /** */
    public void setTitle(String title) {
        this.title = title;
    }

    /** */
    public String getTitle() {
        return title;
    }

    /** */
    private String role;

    /** */
    public void setRole(String role) {
        this.role = role;
    }

    /** */
    public String getRole() {
        return role;
    }

    /** */
    private String phone;

    /** */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /** */
    public String getPhone() {
        return phone;
    }

    /** */
    private String fax;

    /** */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /** */
    public String getFax() {
        return fax;
    }

    /** */
    private String cell;

    /** */
    public void setCell(String cell) {
        this.cell = cell;
    }

    /** */
    public String getCell() {
        return cell;
    }

    /** */
    private String extAdd;

    /** */
    public void setExtAdd(String extAdd) {
        this.extAdd = extAdd;
    }

    /** */
    public String getExtAdd() {
        return extAdd;
    }

    /** */
    private String region;

    /** */
    public void setRegion(String region) {
        this.region = region;
    }

    /** */
    public String getRegion() {
        return region;
    }

    /** */
    private String street;

    /** */
    public void setStreet(String street) {
        this.street = street;
    }

    /** */
    public String getStreet() {
        return street;
    }

    /** */
    private String locality;

    /** */
    public void setLocality(String locality) {
        this.locality = locality;
    }

    /** */
    public String getLocality() {
        return locality;
    }

    /** */
    private String pcode;

    /** */
    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    /** */
    public String getPcode() {
        return pcode;
    }

    /** */
    private String ctry;

    /** */
    public void setCtry(String ctry) {
        this.ctry = ctry;
    }

    /** */
    public String getCtry() {
        return ctry;
    }

    /** */
    private String email;

    /** */
    public void setEmail(String email) {
        this.email = email;
    }

    /** */
    public String getEmail() {
        return email;
    }

    /** */
    private String jabberId;

    /** */
    public void setJabberId(String jabberId) {
        this.jabberId = jabberId;
    }

    /** */
    public String getJabberId() {
        return jabberId;
    }

    /** */
    private String desc;

    /** */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /** */
    public String getDesc() {
        return desc;
    }

    /** */
    private String type;

    /** */
    public void setType(String type) {
        this.type = type;
    }

    /** */
    public String getType() {
        return type;
    }

    /** */
    private String photo;

    /** */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /** */
    public String getPhoto() {
        return photo;
    }

    /** */
    public String getFn() {
        return firstName + " " + surName;
    }

    //----

    static class Tel {
        private String voice;
        public String getVoice() {
            return voice;
        }
        private String number;
        public String getNumber() {
            return number;
        }
    }

    List<VCard.Tel> tels = new ArrayList<VCard.Tel>();

    List<VCard.Tel> getTels() {
        return tels;
    }

    /** */
    public void addFrom(String from) {
    }

    /** @see vavi.net.xmpp.Xmpp#handle(JabberConnection) */
    void handle(JabberConnection jc) throws IOException {
        // TODO Auto-generated method stub
        
    }
}

/* */
