package org.jmanage.webui;

import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class Startup {

    private static final String JMANAGE_PORT = "jmanage.port";
    private static final String JMANAGE_ROOT = "jmanage.root";

    public static void main(String[] args)
        throws Exception {

        int port = Integer.parseInt(System.getProperty(JMANAGE_PORT));
        String webroot = System.getProperty(JMANAGE_ROOT);

        Server server = new Server();
        SocketListener listener = new SocketListener();
        listener.setPort(port);
        server.addListener(listener);
        server.addWebApplication("/", webroot);
        server.start();
    }
}
