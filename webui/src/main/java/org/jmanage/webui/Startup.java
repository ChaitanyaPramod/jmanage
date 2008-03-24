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

import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.auth.User;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.JManageProperties;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.util.PasswordField;
import org.jmanage.util.db.DBUtils;
import org.mortbay.jetty.Server;
import java.io.File;
import java.rmi.RMISecurityManager;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * The Web-UI startup class.
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class Startup {

	private static final Logger logger = Loggers.getLogger(Startup.class);
	
    public static void main(String[] args) throws Exception{
    	String jManageRoot = System.getProperty("JMANAGE_ROOT");
    	CoreUtils.initJmanageForCLIUtilities(jManageRoot);

    	Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                logger.info("jManage shutting down...");
                DBUtils.shutdownDB();
            }
         });
    	
    	if(System.getSecurityManager() == null) {
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
        /* set property for jetty startup indicator */
        System.setProperty("SERVER.IND", "JETTY");
        
        /* initialize crypto */
        Crypto.init(password);
        /* clear the password */
        Arrays.fill(password, ' ');

        /* start the application */
        start(jManageRoot);
    }

    private static void start(String jManageRoot) throws Exception {
    	try{
	    	Server server = new Server(jManageRoot + File.separator + "config" + File.separator + "jetty-config.xml");
	        server.start();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
