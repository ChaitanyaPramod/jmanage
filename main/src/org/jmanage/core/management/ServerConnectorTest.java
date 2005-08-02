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
package org.jmanage.core.management;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigFactory;

import java.util.Set;

/**
 *
 * date:  Aug 18, 2004
 * @author	Rakesh Kalra
 */
public class ServerConnectorTest {

    public static void main(String[] args) throws Exception {
        if(args.length == 0){
            System.out.println("Usage: java ServerConnectorTest [weblogic, tomcat, jsr160]");
            System.exit(0);
        }
        ApplicationConfig appConfig = getApplicationConfig(args[0]);
        ServerConnection connection =
                ServerConnector.getServerConnection(appConfig);
        Set objects = connection.queryNames(new ObjectName("*:*"));
        connection.close();
        System.out.println("number of objects:" + objects.size());
    }

    private static ApplicationConfig getApplicationConfig(String type){
        final ApplicationConfig appConfig =
                ApplicationConfigFactory.create("1234",
                        "test", type,
                        "localhost", new Integer(7001), null,
                        "system", "12345678", null);
        return appConfig;
    }

}
