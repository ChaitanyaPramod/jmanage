package org.jmanage.core.config;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class WeblogicApplicationConfig extends ApplicationConfig {

    public static final String SERVER_NAME = "serverName";
    private static final ConfigParam serverNameParam =
            new ConfigParam(SERVER_NAME, "Server Name");

    private static final List additionalParams = new ArrayList(1);
    static{
        additionalParams.add(serverNameParam);
    }

    private final String serverName;

    public WeblogicApplicationConfig(String applicationId,
                                     String name,
                                     String host,
                                     int port,
                                     String username,
                                     String password,
                                     Map paramValues){
        super(applicationId, name, host, port, username, password, paramValues);
        serverName = (String)paramValues.get(serverNameParam.getName());
    }

    public String getURL() {
        return "t3://" + getHost() + ":" + getPort();
    }

    public String getType(){
        return ApplicationConfig.TYPE_WEBLOGIC;
    }

    public List getAdditionalParameters() {
        return additionalParams;
    }

    public String getServerName(){
        return serverName;
    }
}
