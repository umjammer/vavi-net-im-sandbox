package vavi.xml.jaxp.greenthumb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Locale;

import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * This is an implementation of a SAX parser for Minimal XML (aka SML).
 * <p>
 * Some notes:</p>
 * <ul>
 * <li>Reserved element names are parsed and reported, except for those that
 *     do not match the XML 1.0 Name production</li>
 * <li>The name char checking code is based on the xml-tr2 XmlChar code by
 *     David Brownell</li>
 * <li>Unicode characters &gt; 0xFFFF are not supported, and this includes the
 *     surrogate blocks</li>
 * </ul>
 *
 * @see "http://www.docuverse.com/smldev/index.html"
 *
 * @note Parser is not thread safe
 * @note Prohibited from commercial use, unless discussed with Shawn.
 * @note If you use this in a non-commercial project, all I ask for is credit.
 * @author Shawn Silverman - <a href="mailto:shawn@pobox.com">shawn@pobox.com</a>
 * @author        <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class MinXMLParser implements org.xml.sax.Parser {
    /** DocumentHandler to receive SAX events. */
    protected org.xml.sax.DocumentHandler handler;

    /** Current line number. */
    protected int lineNumber;

    /** Current InputSource. */
    protected InputSource source;

    /** Implements a permanently empty AttributeList. */
    protected static final org.xml.sax.AttributeList emptyAttList = new org.xml.sax.AttributeList() {
        public int getLength() {
            return 0;
        }

        public String getName(int i) {
            return null;
        }

        public String getType(int i) {
            return null;
        }

        public String getType(String name) {
            return null;
        }

        public String getValue(int i) {
            return null;
        }

        public String getValue(String name) {
            return null;
        }
    };

    // Temporary storage
    private StringBuffer nameBuf = new StringBuffer();
    private char[] contentBuf = new char[1024];
    private int contentBufOff;
    private StringBuffer charRefBuf = new StringBuffer();

    // Newline tracking
    private boolean hadCR;

    // For encoding detection
    private boolean startTagStarted;
    private int firstNameChar;

    /**
     * Create a new Minimal XML (aka SML) parser.
     */
    public MinXMLParser() {
        super();
    }

    /**
     * Parses a Minimal XML stream.  This does nothing if the DocumentHandler
     * was not set.
     *
     * @param source the SAX InputSource
     */
    public void parse(InputSource source) throws SAXException, IOException {
        if (handler == null) {
            return;
        }

        this.source = source;

        // Get the Reader
        Reader r = source.getCharacterStream();

        // Do various checks to determine what kind of stream is being used
        if (r == null) {
            InputStream in = source.getByteStream();

            if (in == null) {
                String uri = source.getSystemId();

                if (uri == null) {
                    throw new SAXException("Bad InputSource");
                }

                // Convert the URI string to a URL object
                URL url = new File(uri).isFile() ? new URL("file", null, uri)
                                                 : new URL(uri);

                in = url.openStream();
            }

            // It is here we check for either UTF-8 or UTF-16
            r = detectEncoding(in);
        }

        if (!(r instanceof BufferedReader)) {
            r = new BufferedReader(r);
        }

        // Start the parsing
        // Reset some variables
        lineNumber = 1;
        hadCR = false;

        handler.startDocument();

        // The first name char or '<' might have already been read
        int c;

        if (!startTagStarted) {
            if ('<' != skipWhitespace(r)) {
                reportError("Expecting a \'<\' at document start");
            }

            c = readChar(r);
        } else {
            c = (firstNameChar == 0) ? readChar(r) : firstNameChar;
        }

        parseElement(r, c);

        if (skipWhitespace(r) >= 0) {
            reportError("Extra chars at document end");
        }

        handler.endDocument();
    }

    /**
     * Detects the encoding of the SML stream.  We need only check for UTF-8
     * and UTF-16.  The first character of an SML document can be either
     * whitespace or a '<'.  Here are the possible start combinations:
     * <blockquote><pre>
     * With a Byte Order Mark:
     *  FF FE ## 00: UTF-16, little-endian
     *  FE FF 00 ##: UTF-16, big-endian
     *  EF BB BF: UTF-8
     * Without a Byte Order Mark:  (the 3C can instead be a WS character)
     *  00 3C: UTF-16, big-endian
     *  3C 00: UTF-16, little-endian
     *  3C ^00: UTF-8</pre></blockquote>
     * <p>
     * This method avoids using a pushback buffer by storing the first character.
     */
    private Reader detectEncoding(InputStream in)
        throws IOException, SAXException {
        startTagStarted = false;
        firstNameChar = 0;

        String enc = null;

        switch (in.read()) {
        case 0xFF:
            in.read();
            enc = "UnicodeLittle";
            break;
        case 0xFE:
            in.read();
            enc = "UnicodeBig";
            break;
        case 0xEF:
            in.read();
            in.read();
            enc = "UTF8";
            break;
        case 0x00:
            startTagStarted = (in.read() == 0x3C);
            enc = "UnicodeBig";
            break;
        case 0x3C:
            startTagStarted = true;
            firstNameChar = in.read();
            enc = (firstNameChar == 0x00) ? "UnicodeBig" : "UTF8";
            break;
        case 0x09:
        case 0x20:
        case 0x0D:
        case 0x0A:
            int c = in.read();
            startTagStarted = (c == 0x3C);
            enc = (startTagStarted || isWhitespace(c)) ? "UTF8" : "UnicodeBig";
            if ((c == 0x3C) || (c == 0x00) || isWhitespace(c)) {
                break;
            }
        // fallthrough;
        default:
            reportError("Expecting a \'<\' or whitespace at document start");
        }

        return new InputStreamReader(in, enc);
    }

    /** Reports a parse error. */
    private void reportError(String msg) throws SAXException {
        throw new SAXParseException(msg, null, source.getSystemId(),
                                    lineNumber, -1);
    }

    /**
     * Parses an element.  'c' contains the first character after the '<'
     * element ::= STag content ETag
     */
    private void parseElement(Reader r, int c) throws SAXException, IOException {
        // The first char is already read
        // STag ::= '<' Name '>'
        // Read the name
        // Avoid the extra overhead of another method
        nameBuf.setLength(0);

        if (!isNameStartChar((char) c)) {
            reportError("Invalid name start char: \'" + (char) c + "\'");
        }

        nameBuf.append((char) c);

        while ('>' != (c = readChar(r))) {
            if (!isNameChar((char) c)) {
                reportError("Invalid name char \'" + (char) c +
                            "\' after tag name: " + nameBuf);
            }

            nameBuf.append((char) c);
        }

        // Store the name for later use
        String name = nameBuf.toString();

        handler.startElement(name, emptyAttList);
        parseContent(r);

        // The '</' is already read
        nameBuf.setLength(0);

        while ('>' != (c = readChar(r))) {
            if (!isNameChar((char) c)) {
                reportError("Invalid name char \'" + (char) c +
                            "\' in end tag \'" + nameBuf +
                            "\' for start tag \'" + name + "\'");
            }

            nameBuf.append((char) c);
        }

        if (!name.equals(nameBuf.toString())) {
            reportError("End tag \'" + nameBuf +
                        "\' does not match start tag: \'" + name + "\'");
        }

        handler.endElement(name);
    }

    /**
     * content ::= (element | WS)* | (CharData | CharRef)*
     * NOTE: I don't know what are valid content characters
     */
    private void parseContent(Reader r) throws SAXException, IOException {
        // The '>' from an STag is already read
        while (true) {
            // There could be WS then char data, so we can't use skipWhitespace()
            // Yes we can, because this method now stores the whitespace
            // Nah.. add a trackNewline() method
            contentBufOff = 0;

            int c;

            while (isWhitespace(c = readChar(r))) {
                appendContent((char) c);
            }

            // EOF will be caught by the '/' test
            // Well, that's no longer necessary since not using skipWhitespace() anymore
            // The next guy could either be an element or more char data
            if ('<' != c) {
                do { // Read until another start/end tag

                    if ('&' == c) {
                        if ('#' != readChar(r)) {
                            reportError("Expecting \'#\' in char reference");
                        }

                        // Read the char ref
                        // 0x10FFFF is 1114111 decimal, a maximum of 7 decimal digits plus a ';'
                        charRefBuf.setLength(0);

                        int count = 0;

                        while ((count++ < 8) && (';' != (c = readChar(r)))) {
                            charRefBuf.append((char) c);
                        }

                        if (';' != c) {
                            reportError("Expecting \';\' after char reference: &#" +
                                        charRefBuf);
                        }

                        try {
                            int charRef = Integer.parseInt(charRefBuf.toString(),
                                                           10);

                            if (!isChar(charRef)) {
                                throw new NumberFormatException();
                            }

                            appendContent((char) charRef);
                        } catch (NumberFormatException ex) {
                            reportError("Invalid char reference: &#" +
                                        charRefBuf + ";");
                        }
                    } else if ('>' == c) {
                        reportError("Can\'t have a \'>\' in char data");
                    } else {
                        appendContent((char) c);
                    }

                    c = readChar(r);

                    // Read until the next start/end tag
                } while ('<' != c);
            } else {
                contentBufOff = 0;
            }

            // Parse another element if we didn't encounter an end tag
            if ('/' != (c = readChar(r))) {
                if (contentBufOff > 0) {
                    handler.characters(contentBuf, 0, contentBufOff);
                }

                parseElement(r, c);
            } else {
                break;
            }
        }

        if (contentBufOff > 0) {
            handler.characters(contentBuf, 0, contentBufOff);
        }
    }

    // Skips whitespace and returns the first non-whitespace character.
    private int skipWhitespace(Reader r) throws IOException {
        while (true) {
            int c;
            trackNewline(c = r.read());

            if ((' ' != c) && ('\n' != c) && ('\t' != c) && ('\r' != c)) {
                return c;
            }
        }
    }

    // Keeps track of lines
    // This must be called consecutively on all characters
    private void trackNewline(int c) {
        if ('\n' == c) {
            hadCR = false;
            lineNumber++;
        } else if ('\r' == c) {
            // Keep track of different newline combinations
            if (hadCR) {
                lineNumber++;
            } else {
                hadCR = true;
            }
        } else {
            // Increment the line if we have to
            if (hadCR) {
                lineNumber++;
            }
        }
    }

    /**
     * Reads a single char, catching any EOF.
     */
    private int readChar(Reader r) throws SAXException, IOException {
        int c;

        if ((c = r.read()) >= 0) {
            trackNewline(c);

            return c;
        }

        reportError("Unexpected EOF");

        return 0; // Extraneous
    }

    /** Appends to the content buffer. */
    private void appendContent(char c) {
        // Append this way because 'try's are free
        while (true) {
            try {
                contentBuf[contentBufOff] = c;
                contentBufOff++;

                break;
            } catch (ArrayIndexOutOfBoundsException ex) {
                char[] newBuf = new char[contentBuf.length << 1];
                System.arraycopy(contentBuf, 0, newBuf, 0, contentBufOff);
                contentBuf = newBuf;
            }
        }
    }

    /** Tests if the char is whitespace */
    private static boolean isWhitespace(int c) {
        return ((c == ' ') || (c == '\n') || (c == '\r') || (c == '\t'));
    }

    /** Determines if the char is a valid XML char */
    private static boolean isChar(int ch) {
        return ((ch >= 0x0020) && (ch <= 0xD7FF)) ||
               ((ch == 0x000A) || (ch == 0x0009) || (ch == 0x000D)) ||
               ((ch >= 0xE000) && (ch <= 0xFFFD)) ||
               ((ch >= 0x10000) && (ch <= 0x10FFFF));
    }

    /**
     * Character specific section
     * Code is blatantly similar to Sun's xml-tr2 parser
     * Credit goes to David Brownell, the author of this
     */
    private static boolean isNameStartChar(char ch) {
        // Optimize the typical case
        if (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))) {
            return true;
        }

        switch (Character.getType(ch)) {
        case Character.LOWERCASE_LETTER: // Ll
        case Character.UPPERCASE_LETTER: // Lu
        case Character.OTHER_LETTER: // Lo
        case Character.TITLECASE_LETTER: // Lt
        case Character.LETTER_NUMBER: // Nl
            return !isCompatibilityChar(ch) &&
                   !((ch >= 0x20dd) && (ch <= 0x20e0));
        default:
            return  (ch == '_')    || (ch == ':')     ||
                   ((ch >= 0x02bb) && (ch <= 0x02c1)) || (ch == 0x0559) ||
                    (ch == 0x06e5) || (ch == 0x06e6);
        }
    }

    private static boolean isNameChar(char ch) {
        return isLetter(ch) || isDigit(ch) || (ch == '.') || (ch == '-') ||
               (ch == '_') || (ch == ':') || isExtender(ch);
    }

    private static boolean isLetter(char ch) {
        // Optimize the typical case
        if (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))) {
            return true;
        }

        switch (Character.getType(ch)) {
        case Character.LOWERCASE_LETTER: // Ll
        case Character.UPPERCASE_LETTER: // Lu
        case Character.OTHER_LETTER: // Lo
        case Character.TITLECASE_LETTER: // Lt
        case Character.LETTER_NUMBER: // Nl
        case Character.COMBINING_SPACING_MARK: // Mc
        case Character.ENCLOSING_MARK: // Me
        case Character.NON_SPACING_MARK: // Mn
        case Character.MODIFIER_LETTER: // Lm
        case Character.DECIMAL_DIGIT_NUMBER: // Nd
            return !isCompatibilityChar(ch) &&
                   !((ch >= 0x20dd) && (ch <= 0x20e0));
        default:
            return ch == 0x0387;
        }
    }

    private static boolean isDigit(char ch) {
        return Character.isDigit(ch) && !((ch >= 0xff10) && (ch <= 0xff19));
    }

    private static boolean isExtender(char ch) {
        return  (ch == 0x00b7) || (ch == 0x02d0)  || (ch == 0x02d1) ||
                (ch == 0x0387) || (ch == 0x0640)  || (ch == 0x0e46) ||
                (ch == 0x0ec6) || (ch == 0x3005)  ||
               ((ch >= 0x3031) && (ch <= 0x3035)) ||
               ((ch >= 0x309d) && (ch <= 0x309e)) ||
               ((ch >= 0x30fc) && (ch <= 0x30fe));
    }

    /**
     * This code is strikingly similar to the same method in
     * com.sun.xml.util.XmlChars, inside the xml-tr2 package,
     * written by David Brownell.
     */
    private static boolean isCompatibilityChar(char ch) {
        switch ((ch >> 8) & 0x0ff) {
        case 0x00:
            // ISO Latin/1 has a few compatibility characters
            return (ch == 0x00aa) || (ch == 0x00b5) || (ch == 0x00ba);
        case 0x01:
            // as do Latin Extended A and (parts of) B
            return ((ch >= 0x0132) && (ch <= 0x0133)) ||
                   ((ch >= 0x013f) && (ch <= 0x0140)) || (ch == 0x0149) ||
                   (ch == 0x017f) || ((ch >= 0x01c4) && (ch <= 0x01cc)) ||
                   ((ch >= 0x01f1) && (ch <= 0x01f3));
        case 0x02:
            // some spacing modifiers
            return ((ch >= 0x02b0) && (ch <= 0x02b8)) ||
                   ((ch >= 0x02e0) && (ch <= 0x02e4));
        case 0x03:
            return ch == 0x037a; // Greek
        case 0x05:
            return ch == 0x0587; // Armenian
        case 0x0e:
            return (ch >= 0x0edc) && (ch <= 0x0edd); // Laotian
        case 0x11:
            // big chunks of Hangul Jamo are all "compatibility"
            return (ch == 0x1101) || (ch == 0x1104) || (ch == 0x1108) ||
                   (ch == 0x110a) || (ch == 0x110d) ||
                   ((ch >= 0x1113) && (ch <= 0x113b)) || (ch == 0x113d) ||
                   (ch == 0x113f) || ((ch >= 0x1141) && (ch <= 0x114b)) ||
                   (ch == 0x114d) || (ch == 0x114f) ||
                   ((ch >= 0x1151) && (ch <= 0x1153)) ||
                   ((ch >= 0x1156) && (ch <= 0x1158)) || (ch == 0x1162) ||
                   (ch == 0x1164) || (ch == 0x1166) || (ch == 0x1168) ||
                   ((ch >= 0x116a) && (ch <= 0x116c)) ||
                   ((ch >= 0x116f) && (ch <= 0x1171)) || (ch == 0x1174) ||
                   ((ch >= 0x1176) && (ch <= 0x119d)) ||
                   ((ch >= 0x119f) && (ch <= 0x11a2)) ||
                   ((ch >= 0x11a9) && (ch <= 0x11aa)) ||
                   ((ch >= 0x11ac) && (ch <= 0x11ad)) ||
                   ((ch >= 0x11b0) && (ch <= 0x11b6)) || (ch == 0x11b9) ||
                   (ch == 0x11bb) || ((ch >= 0x11c3) && (ch <= 0x11ea)) ||
                   ((ch >= 0x11ec) && (ch <= 0x11ef)) ||
                   ((ch >= 0x11f1) && (ch <= 0x11f8));
        case 0x20:
            return ch == 0x207f; // superscript
        case 0x21:
            return (
            // various letterlike symbols
            ch == 0x2102) || (ch == 0x2107) ||
                   ((ch >= 0x210a) && (ch <= 0x2113)) || (ch == 0x2115) ||
                   ((ch >= 0x2118) && (ch <= 0x211d)) || (ch == 0x2124) ||
                   (ch == 0x2128) || ((ch >= 0x212c) && (ch <= 0x212d)) ||
                   ((ch >= 0x212f) && (ch <= 0x2138))// most Roman numerals (less 1K, 5K, 10K)
                    || ((ch >= 0x2160) && (ch <= 0x217f));
        case 0x30:
            // some Hiragana
            return (ch >= 0x309b) && (ch <= 0x309c);
        case 0x31:
            // all Hangul Compatibility Jamo
            return (ch >= 0x3131) && (ch <= 0x318e);
        case 0xf9:
        case 0xfa:
        case 0xfb:
        case 0xfc:
        case 0xfd:
        case 0xfe:
        case 0xff:
            // the whole "compatibility" area is for that purpose!
            return true;
        default:
            // most of Unicode isn't flagged as being for compatibility
            return false;
        }
    }

    /*
     * Extra SAX stuff
     */
    public void parse(String systemId) throws SAXException, IOException {
        parse(new InputSource(systemId));
    }

    /**
     * Registers a document handler.
     */
    public void setDocumentHandler(org.xml.sax.DocumentHandler handler) {
        this.handler = handler;
    }

    /**
     * Does nothing.
     */
    public void setDTDHandler(DTDHandler handler) {
    }

    /**
     * Does nothing.
     */
    public void setEntityResolver(EntityResolver resolver) {
    }

    /**
     * Does nothing.
     */
    public void setErrorHandler(ErrorHandler handler) {
    }

    public void setLocale(Locale locale) throws SAXException {
        throw new SAXException("Not supported");
    }
}

/* */