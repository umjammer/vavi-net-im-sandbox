/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.xml.jaxp.greenthumb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


/**
 * DocumentBuilderImpl.
 *
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	040121	nsano	initial version <br>
 */
public class DocumentBuilderImpl extends DocumentBuilder {

    /** */
    @SuppressWarnings("unused")
    private DocumentBuilderFactory dbf;

    /** */
    private EntityResolver er = null;
    /** */
    private ErrorHandler eh = null;
    /** */
//    private Object domParser = null;
    private MinXMLParser domParser;

    /** */
    @SuppressWarnings("unused")
    private boolean namespaceAware = false;
    /** */
    private boolean validating;

    /** */
    DocumentBuilderImpl(DocumentBuilderFactory dbf)
        throws ParserConfigurationException {

        this.dbf = dbf;

//        try {
//          String parser = (String)
//              dbf.getAttribute("http://cyberneko.org/dom/attribute/parser");
//          if ("fragment".equalsIqnoreCase(parser)) {
//              domParser = new DOMFragmentParser();
//          } else {
                domParser = new MinXMLParser();
//	        }

            // Validation   
            validating = dbf.isValidating();
            String validation = "http://xml.org/sax/features/validation";
            features.put(validation, validating);
//        } catch (SAXException e) {
            // Handles both SAXNotSupportedException, SAXNotRecognizedException
//            throw new ParserConfigurationException(e.getMessage());
//        }
    }

    /** */
    private Map<String, Boolean> features = new HashMap<>();
    
    /** */
    public Document newDocument() {
        return null; 
    }

    /** */
    public DOMImplementation getDOMImplementation() {
        return null;
    }

    /** */
    public Document parse(InputSource is) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException("InputSource cannot be null");
        }

        if (er != null) {
            ((XMLReader) domParser).setEntityResolver(er);
        }

        if (eh != null) {
            ((XMLReader) domParser).setErrorHandler(eh);      
        }

//      if (domParser instanceof DOMFragmentParser) {
//          DOMFragmentParser parser = (DOMFragmentParser) domParser;
//          parser.parse(is);
//          return parser.getDocument();
//      } else {
            MinXMLParser parser = domParser;
            parser.setDocumentHandler(handler);
            parser.parse(is);
            return document;
//      }
    }

    /** */
    private Document document;
    
    /** */
    public boolean isNamespaceAware() {
        return false;
    }

    /** */
    public boolean isValidating() {
        return validating;
    }

    /** */
    public void setEntityResolver(EntityResolver er) {
        this.er = er;
    }

    /** */
    public void setErrorHandler(ErrorHandler eh) {
        // If app passes in a ErrorHandler of null, then ignore all errors
        // and warnings
        this.eh = (eh == null) ? new DefaultHandler() : eh;
    }
    
    /** */
    @SuppressWarnings("deprecation")
    private org.xml.sax.DocumentHandler handler = new org.xml.sax.DocumentHandler() {

        public void setDocumentLocator(Locator locator) {
            // TODO Auto-generated method stub
            
        }

        public void startDocument() throws SAXException {
        }

        public void endDocument() throws SAXException {
        }

        public void startElement(String name, org.xml.sax.AttributeList atts) throws SAXException {
        }

        public void endElement(String name) throws SAXException {
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        }

        public void processingInstruction(String target, String data) throws SAXException {
        }
    };
}

/* */
