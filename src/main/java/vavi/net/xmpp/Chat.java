/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.xmpp;


/**
 * Chat.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040117 nsano initial version <br>
 */
public class Chat {

    /** */
    public Chat(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /** */
    private String name;

    /** */
    public String getName() {
        return name;
    }

    /** */
    private String type;

    /** */
    public String getType() {
        return type;
    }

    /** */
    public void processIncoming(String from, String text, String subject) {
        text = unescapeHtml(text);
        if (subject != null) {
            if (!subject.equals("")) {
//                subjectLine.setText(subject);
            }
        }
        if (text != null) {
            if (!text.equals("")) {
                print("", text);
            }
        }
    }

    /** TODO */
    private String exN(String f) {
        int i = f.indexOf("/");
        if (!(i < 0)) {
            f = f.substring(i + 1);
        }
        return f;
    }

    /** */
    public String unescapeHtml(String text) {
        String val = text.replaceAll("&amp;", "&");
        val = val.replaceAll("&apos;", "'");
        val = val.replaceAll("&quot;", "\"");
        val = val.replaceAll("&lt;", "<");
        val = val.replaceAll("&gt;", ">");
        return val;
    }

    /** */
    public void processIncoming(String from, String text) {
        text = unescapeHtml(text);
        if (text.startsWith("/me ")) {
            if (type.equals("chat")) {
                text = "* " + from + " " + text.substring(4);
            } else {
                text = "* " + exN(from) + " " + text.substring(4);
            }
            print(text);
        } else {
            print(from, text);
        }
        if (text.startsWith("/url ")) {
            String u = text.substring(5);
//            p.getAppletContext().showDocument(new URL("javascript:alert('" + text + "')"));
        }
        if (text.startsWith("/sound ")) {
//            soundfile = text.substring(7);
        }
    }

    /** */
    private void print(String text) {
    }

    /** */
    private void print(String from, String text) {
    }
}

/* */
