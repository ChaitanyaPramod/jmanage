package org.jmanage.core.config;

import java.util.*;

/**
 *
 * date:  Jun 13, 2004
 * @author	Rakesh Kalra
 */
public class ApplicationConfigManager{

    private static final Map applicationConfigs =
            Collections.synchronizedMap(new HashMap());

    private static final ConfigReader configReader = ConfigReader.getInstance();

    static{
        /*  Currently supporting only Weblogic (Weblogic 6.1)   */
        configReader.loadApplications(applicationConfigs);
    }

    public static ApplicationConfig getApplicationConfig(String applicationId){
        return (ApplicationConfig)applicationConfigs.get(applicationId);
    }

    /**
     * Retrieve all configured applications.
     *
     * @return
     */
    public static Map getApplications(){
        return applicationConfigs;
    }
}
