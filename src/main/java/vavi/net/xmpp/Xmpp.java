/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.xmpp;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

import org.apache.commons.betwixt.io.BeanWriter;

import vavi.util.Debug;


/**
 * Xmpp.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040125 nsano initial version <br>
 */
public abstract class Xmpp implements Serializable {

    /** */
    protected String xmlns;

    /** */
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    /** */
    public String getXmlns() {
        return xmlns;
    }

    /** */
    protected abstract String getXmlName();

    /** TODO */
    abstract void handle(JabberConnection jc) throws IOException;

    /** */
    public String toString() {
        try {
            StringWriter outputWriter = new StringWriter();

            BeanWriter beanWriter = new BeanWriter(outputWriter);

            beanWriter.getXMLIntrospector().setAttributesForPrimitives(true);
            beanWriter.enablePrettyPrint();

            beanWriter.write(getXmlName(), this);

            return outputWriter.toString();
        } catch (Exception e) {
Debug.printStackTrace(e);
	        throw new IllegalStateException(e);
        }
    }
}

/* */
