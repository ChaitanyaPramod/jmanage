package org.jmanage.core.management;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigFactory;
import org.jmanage.core.config.ApplicationType;

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
        final ApplicationConfig appConfig =
                ApplicationConfigFactory.create("1234",
                        "test", ApplicationType.WEBLOGIC,
                        "localhost", new Integer(7001), null,
                        "system", "12345678", null);
        return appConfig;
    }
}
