package org.jmanage.core.config;

import java.util.Map;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class ApplicationConfigFactory {


    public static ApplicationConfig create(String name,
                                           String type,
                                           String host,
                                           int port){
        return create(name, type, host, port, null, null, null);
    }

    public static ApplicationConfig create(String name,
                                           String type,
                                           String host,
                                           int port,
                                           String username,
                                           String password,
                                           Map paramValues){

        if(type.equals(ApplicationConfig.TYPE_WEBLOGIC)){
            return new WeblogicApplicationConfig(name, host, port,
                    username, password, paramValues);
        }else{
            throw new RuntimeException("Invalid application type: " + type);
        }
    }
}
