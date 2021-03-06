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

import org.mortbay.jetty.Server;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.util.PasswordField;
import org.jmanage.core.util.JManageProperties;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.auth.User;
import org.jmanage.core.auth.ACLStore;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.alert.AlertEngine;
import org.jmanage.connector.framework.ConnectorConfigRegistry;
import org.jmanage.connector.framework.ConnectorRegistry;
import org.jmanage.monitoring.downtime.ApplicationDowntimeService;
import org.jmanage.util.db.DBUtils;

import java.rmi.RMISecurityManager;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

/**
 * The Web-UI startup class.
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class Startup {

    private static final Logger logger = Loggers.getLogger(Startup.class);
    
    public static void main(String[] args) throws Exception{

        Runtime.getRuntime().addShutdownHook(new Thread(){
           public void run(){
               logger.info("jManage shutting down...");
               DBUtils.shutdownDB();
           }
        });
        
        if (System.getSecurityManager() == null) {
           System.setSecurityManager(new RMISecurityManager());
        }
    
        UserManager userManager = UserManager.getInstance();
        User user = null;
        char[] password = null;
        int invalidAttempts = 0;

        if(args.length == 1){
            password = args[0].toCharArray();
            user = userManager.verifyUsernamePassword(
                    AuthConstants.USER_ADMIN, password);
            /* invalid password was tried */
            if(user == null){
                invalidAttempts ++;
            }
        }

        while(user == null){
            if(invalidAttempts > 0){
                System.out.println("Invalid Admin Password.");
            }
            /* get the password */
            password = PasswordField.getPassword("Enter password [default: 123456]:");
            /* the password should match for the admin user */
            user = userManager.verifyUsernamePassword(
                    AuthConstants.USER_ADMIN, password);
            invalidAttempts ++;
            if(invalidAttempts >= 3){
                break;
            }
        }

        /* exit if the admin password is still invalid */
        if(user == null){
            System.out.println("Number of invalid attempts exceeded. Exiting !");
            return;
        }

        /* set admin password as the stop key */
        final JettyStopKey stopKey = new JettyStopKey(new String(password));
        System.setProperty("STOP.KEY", stopKey.toString());
        /* set stop.port */
        System.setProperty("STOP.PORT", JManageProperties.getStopPort());

        DBUtils.init();
        /* initialize ServiceFactory */
        ServiceFactory.init(ServiceFactory.MODE_LOCAL);

        /* initialize crypto */
        Crypto.init(password);

        /* clear the password */
        Arrays.fill(password, ' ');

        /* load ACLs */
        ACLStore.getInstance();

        /* start the AlertEngine */
        AlertEngine.getInstance().start();

        /* start the application downtime service */
        ApplicationDowntimeService.getInstance().start();
        
        
        /* load connectors */
        try{
            ConnectorConfigRegistry.init();
        }catch(Exception e){
            logger.log(Level.SEVERE, "Error initializing connector config registry.", e);
        }
        try{
            ConnectorRegistry.load();
        }catch(Exception e){
            logger.log(Level.SEVERE, "Error initializing connector registry.", e);
        }
        /* start the application */
        start();
    }

    private static void start() throws Exception {
        Server server =
                new Server(CoreUtils.getConfigDir() +
                File.separator + "jetty-config.xml");
        ServerMonitor.monitor();
        server.start();
    }
}
