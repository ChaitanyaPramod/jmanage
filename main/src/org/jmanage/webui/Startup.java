/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.webui;

import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.WebApplicationContext;
import org.mortbay.http.SunJsseListener;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.crypto.PasswordField;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.auth.User;
import org.jmanage.core.auth.ACLStore;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.config.JManageProperties;

import java.util.Arrays;

/**
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
            user = userManager.verifyUsernamePassword(AuthConstants.USER_ADMIN, password);
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

        /* initialize ServiceFactory */
        ServiceFactory.init(ServiceFactory.MODE_LOCAL);
        /* initialize crypto */
        Crypto.init(password);
        /* clear the password */
        Arrays.fill(password, ' ');
        /* load ACLs */
        ACLStore.init();

        /* start the application */
        start();
    }

    private static void start()
            throws Exception {

        int port = JManageProperties.getPort().intValue();
        Integer sslPort = JManageProperties.getSslPort();
        String webroot = CoreUtils.getWebDir();
        Server server = new Server();
        if(sslPort!=null){
            SunJsseListener listener = new SunJsseListener();
            String path = CoreUtils.getConfigDir() + "/" + JManageProperties.getKeystrokeFile();
            listener.setPort(sslPort.intValue());
            listener.setKeystore(path);
            listener.setPassword(JManageProperties.getSSLPassword());
            listener.setKeyPassword(JManageProperties.getSSLKeyPassword());
            server.addListener(listener);
        }else{
            SocketListener listener = new SocketListener();
            listener.setPort(port);
            server.addListener(listener);
        }
        WebApplicationContext webAppContext = server.addWebApplication("/", webroot);
        webAppContext.setClassLoaderJava2Compliant(true);
        server.start();
    }
}
