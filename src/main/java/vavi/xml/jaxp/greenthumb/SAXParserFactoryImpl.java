/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.xml.jaxp.greenthumb;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


/**
 * SAXParserFactoryImpl.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040121 nsano initial version <br>
 */
public class SAXParserFactoryImpl extends SAXParserFactory {

    /** */
    public SAXParserFactoryImpl() {
    }
    
    /** */
    public SAXParser newSAXParser()
        throws SAXException, ParserConfigurationException {

        return new SAXParserImpl();
    }

    /** */
    public void setFeature(String name, boolean value) {
        throw new IllegalStateException("not implemented");
    }

    /** */
    public boolean getFeature(String name) {
        throw new IllegalStateException("not implemented");
    }
}

/* */
