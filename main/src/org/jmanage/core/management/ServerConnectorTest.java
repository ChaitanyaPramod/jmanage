package org.jmanage.core.management;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.WeblogicApplicationConfig;

import java.util.Set;

/**
 *
 * date:  Aug 18, 2004
 * @author	Rakesh Kalra
 */
public class ServerConnectorTest {

    public static void main(String[] args){
        ApplicationConfig appConfig = getApplicationConfig();
        ServerConnection connection =
                ServerConnector.getServerConnection(appConfig);
        Set objects = connection.queryObjects(new ObjectName("*:*"));
        System.out.println("number of objects:" + objects.size());
    }

    private static ApplicationConfig getApplicationConfig(){
        final WeblogicApplicationConfig appConfig =
                new WeblogicApplicationConfig("1234",
                        "test", "localhost", 7001,
                        "system", "12345678", null);
        return appConfig;
    }
}
