/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.xml.jaxp.greenthumb;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;


/**
 * This implements JAXP parser interface for MinXMLParser.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040121 nsano initial version <br>
 */
public class SAXParserImpl extends SAXParser {

    /** */
    @SuppressWarnings("deprecation")
    private org.xml.sax.Parser parser;

    /** */
    SAXParserImpl() throws SAXException, ParserConfigurationException {
        parser = new MinXMLParser();
    }
    
    /** */
    @SuppressWarnings("deprecation")
    public org.xml.sax.Parser getParser() throws SAXException {
        return parser;
    }

    /** */
    public XMLReader getXMLReader() throws SAXException {
        return null;
    }

    /** */
    public void setProperty(String name, Object value) {
System.err.println("not implemented");
    }

    /** */
    public Object getProperty(String name) {
        throw new IllegalArgumentException("not implemented");
    }

    /** */
    public boolean isNamespaceAware() {
        return false;
    }

    /** */
    public boolean isValidating() {
        return false;
    }
}

/* */
