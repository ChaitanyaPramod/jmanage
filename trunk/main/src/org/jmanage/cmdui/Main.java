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
package org.jmanage.cmdui;

import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.util.JManageProperties;
import org.jmanage.core.util.Loggers;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.ConsoleHandler;
import java.security.Security;

/**
 *
 * jmanage -username admin -password 123456 -url <jmanage-url> <command> <command args>
 * <p>
 * commands:
 * <p>
 * <li>apps
 * <li>mbeans     &lt;appName&gt; [filter expression]
 * <li>cmbeans    &lt;appName&gt;
 * <li>info       &lt;appName&gt;/&lt;mbeanName[configured name or object name]&gt;
 * <li>execute    &lt;appName&gt;/&lt;mbeanName&gt;/&lt;operationName&gt; [args]
 * <li>print      &lt;appName&gt;/&lt;mbeanName&gt;/attributeName1 [attributeName2]
 * <li>get        &lt;appName&gt;/&lt;mbeanName&gt; &lt;attributeName&gt; newValue
 * <li>set        &lt;appName&gt;/&lt;mbeanName&gt; &lt;attributeName&gt; newValue
 * <li>setattrs   &lt;appName&gt;/&lt;mbeanName&gt; &lt;attributeName&gt;=newValue ...
 *
 * TODO:
 *
 * serverinfo
 * register
 * unregister
 *
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class Main {

    private static final Logger logger = Loggers.getLogger(Main.class);

    static{
        /* initialize ServiceFactory */
        if(JManageProperties.getJManageURL() == null){
            /* run the factory in local mode */
            ServiceFactory.init(ServiceFactory.MODE_LOCAL);
        }else{
            ServiceFactory.init(ServiceFactory.MODE_REMOTE);
        }
    }

    public static void main(String[] args)
        throws Exception {

        Command command = Command.get(args);

        /* setup logging */
        setLogging(command);

        /* authenticate the user */
        if(command.isAuthRequired()){
            if(!command.authenticate()){
                System.out.println("Authentication failed.");
                return;
            }
        }

        if(command.getName() == null){
            /* get into the prompt mode */
            PromptMode promptMode = new PromptMode(command);
            promptMode.start();
        }else{
            /* execute command */
            command.execute();
        }
    }

    private static void setLogging(Command command){
        LogManager logManager = LogManager.getLogManager();
        logManager.reset();
        /* set the log level on the root logger */
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(command.getLogLevel());
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(command.getLogLevel());
        rootLogger.addHandler(consoleHandler);

        logger.fine("Log level=" + command.getLogLevel());
    }
}
