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

    public static void main(String[] args){
        if(args.length == 0){
            System.out.println("Usage: java ServerConnectorTest [weblogic, tomcat, jsr160]");
            System.exit(0);
        }
        ApplicationConfig appConfig = getApplicationConfig(args[0]);
        ServerConnection connection =
                ServerConnector.getServerConnection(appConfig);
        Set objects = connection.queryObjects(new ObjectName("*:*"));
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
