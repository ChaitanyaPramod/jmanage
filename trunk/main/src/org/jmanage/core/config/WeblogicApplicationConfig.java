package org.jmanage.core.config;

import java.util.Map;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class WeblogicApplicationConfig extends AbstractApplicationConfig {

    public static final String SERVER_NAME = "serverName";

    private String serverName;

    public WeblogicApplicationConfig(String name, String host, int port,
                                     String username, String password,
                                     Map attributes){
        super(name, host, port, username, password);
        serverName = (String)attributes.get(SERVER_NAME);
    }

    public String getURL() {
        return "t3://" + host + ":" + port;
    }

    public String getType(){
        return ApplicationConfig.TYPE_WEBLOGIC;
    }

    public String getServerName(){
        return serverName;
    }
}
