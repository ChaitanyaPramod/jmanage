package org.jmanage.webui;

import org.mortbay.http.SocketListener;
import org.mortbay.http.HttpContext;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.WebApplicationContext;
import org.jmanage.core.util.SystemProperties;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.crypto.PasswordField;
import org.jmanage.core.crypto.Crypto;

import java.util.Arrays;
import java.io.IOException;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class Startup {

    public static void main(String[] args)
            throws Exception {

        /* get the password */
        final char[] password = PasswordField.getPassword("Enter password:");
        /* initialize crypto */
        Crypto.init(password);
        /* clear the password */
        Arrays.fill(password, ' ');

        /* start the application */
        start();
    }

    private static void start()
            throws Exception {

        int port = Integer.parseInt(System.getProperty(SystemProperties.JMANAGE_PORT));
        String webroot = CoreUtils.getWebDir();
        Server server = new Server();
        SocketListener listener = new SocketListener();
        listener.setPort(port);
        server.addListener(listener);
        WebApplicationContext webAppContext = server.addWebApplication("/", webroot);
        webAppContext.setClassLoaderJava2Compliant(true);
        server.start();
    }
}
