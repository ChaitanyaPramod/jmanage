package org.jmanage.webui;

import org.mortbay.http.SocketListener;
import org.mortbay.http.HttpContext;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.WebApplicationContext;
import org.jmanage.core.util.SystemProperties;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.crypto.PasswordField;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.auth.User;

import java.util.Arrays;
import java.io.IOException;

/**
 * TODO: need to remove the xerces usage from weblogic.jar
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class Startup {

    public static void main(String[] args)
            throws Exception {

        UserManager userManager = UserManager.getInstance();
        User user = null;
        char[] password = null;
        int invalidAttempts = 0;
        do{
            if(invalidAttempts > 0){
                System.out.println("Invalid Admin Password.");
            }
            /* get the password */
            password = PasswordField.getPassword("Enter password:");
            /* the password should match for the admin user */
            user = userManager.getUser(AuthConstants.USER_ADMIN, password);
            invalidAttempts ++;
            if(invalidAttempts >= 3){
                break;
            }
        }while(user == null);

        /* exit if the admin password is still invalid */
        if(user == null){
            System.out.println("Number of invalid attempts exceeded. Exiting !");
            return;
        }

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
