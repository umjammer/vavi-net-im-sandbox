/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.xmpp;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * Poller.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040117 nsano initial version <br>
 */
class Poller extends Thread {

    public JabberConnection jc;

    private boolean keepPolling = true;
    private int pollingInterval = 2500;
    private final int minPollingInterval = 2500;
    private final int maxPollingInterval = 30000;
    private URL pollingURL;
    private String sessionCookie;

    /** */
    public Poller(JabberConnection jc, URL pollingURL) {
        this.jc = jc;
        this.pollingURL = pollingURL;
    }

    /**
     * send a polling request to the server and send data to it
     * @param send data to be sent
     */
    synchronized void poll(String send) throws IOException {

        // get a HttpURLConnection and check if it is a HTTP URL
        URLConnection urlTemp = pollingURL.openConnection();
        if (!(urlTemp instanceof HttpURLConnection)) {
System.out.println("URL ist not a HTTP URL");
            disconnect();
            return ;
        }
        HttpURLConnection urlConn = (HttpURLConnection) urlTemp;
        // we have to POST the data
        urlConn.setRequestMethod("POST");
        // we send output
        urlConn.setDoInput(true);
        // we want read the reply
        urlConn.setDoOutput(true);
        // pass caches
        urlConn.setUseCaches(false);
        // allow the user to enter passwords if necessary
        urlConn.setAllowUserInteraction(true);
        // we POST with a MIME type that is not really correct
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // output session cookie "0" for new connection
        OutputStream out = urlConn.getOutputStream();
        if (sessionCookie == null) { // new session, no cookie yet
            out.write("0,".getBytes("UTF8"));
        } else { // existing session and cookie
            out.write((sessionCookie + ",").getBytes("UTF8"));
        }
        // do we have to send real content?
        if (send != null) {
            out.write(send.getBytes("UTF8"));
        }
        // flush and close the output so that the receiver gets an EOF
        out.flush();
        out.close();
        InputStream in = urlConn.getInputStream();
        // read the new session cookie
        sessionCookie = urlConn.getHeaderField("Set-Cookie");
        if (sessionCookie != null) {
            if (sessionCookie.substring(0, 3).equals("ID=")) {
                sessionCookie = sessionCookie.substring(3);
            }
            int index = sessionCookie.indexOf(';');
            if (index > 0) {
                sessionCookie = sessionCookie.substring(0, index - 1);
            }
        }
        // check HTTP response code (should be 200)
        if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
System.out.println("HTTP response code is: " + (new Integer(urlConn.getResponseCode())).toString());
            disconnect();
        }
        // is it an error reply?
        if (sessionCookie.endsWith(":0")) {
System.out.println("Got error cookie: " + sessionCookie);
            disconnect();
        } else {
System.out.println("Got session cookie: " + sessionCookie);
        }
        // read the input
        long bytesReceived = 0;
        int count;
        byte[] b = new byte[1024];
        while (-1 != (count = in.read(b, 0, 1024))) {
            if (count > 0) {
                String resp = new String(b, 0, count, "UTF8");
                bytesReceived += count;
// TODO                jc.dataRecieved(resp);
            } else { // we have to wait a bit for the reply
                try { Thread.sleep(1000); } catch (Exception ie) {}
            }
        }
        in.close();
        // recalculate the polling interval, adjust by 10%
        int tenthOfInterval = pollingInterval / 10;
        // if we got something real it should be bigger then 2 bytes
        // if we get 1 or 2 bytes it's maybe just a keep-alive space
        if (bytesReceived > 2) {
            pollingInterval -= tenthOfInterval;
        } else {
            pollingInterval += tenthOfInterval;
        }
        // check if the new interval is okay
        if (pollingInterval > maxPollingInterval) {
            pollingInterval = maxPollingInterval;
        }
        if (pollingInterval < minPollingInterval) {
            pollingInterval = minPollingInterval;
        }
    }

    /** send a polling request to the server */
    void poll() {
        try {
            poll(null);
        } catch (IOException e) {
e.printStackTrace(System.err);
            disconnect();
        }
    }

    /** Look for new data packets from Server. */
    public void run() {
        while (keepPolling) {
System.out.println("PollingLoop");
            // if (moConnState.state() == moConnState.OPEN) {
            poll();
            // }
            try { Thread.sleep(pollingInterval); } catch (Exception e) {}
        }
    }

    /**
     * Sends data packets to Server
     * @param str text to send to server
     * @return true if it was able to send data, false otherwise
     */
    public boolean send(String str) throws IOException {
        poll(str);
        return true;
    }

    /**
     * Open virtual connection to the server. We don't really but set the
     * state to connected and start the polling loop.
     * @param host Host Name or IP Address (Ignored for Poller)
     * @param port Port to connect to (Ignored for Poller)
     * @return the new connection state public ConnectionState
     * connect(String host, int port) {
     * moConnState.state(ConnectionState.OPEN); return moConnState; }
     */

    /** Disconnect logical connection to the server */
    public void disconnect() {
System.out.println("Poller.disconnect() called");

//        moConnState.state(moConnState.CLOSING);
//        
//        if (moConnectionClient != null){
//            moConnectionClient.connectionClosed();
//        }
//        
//        moConnState.state(moConnState.CLOSED);
    }

    /**
     * Prepare the instance of Poller to be removed.
     * This will stop the polling loop.
     */
    public void cleanup() {
        disconnect();
        keepPolling = false;
    }
}

/* */
