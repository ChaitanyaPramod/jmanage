package org.jmanage.core.config;

import java.util.*;

/**
 *
 * date:  Jun 13, 2004
 * @author	Rakesh Kalra
 */
public class ApplicationConfigManager{

    private static List applicationConfigs = null;

    private static final ConfigReader configReader = ConfigReader.getInstance();

    static{
        /*  Currently supporting only Weblogic (Weblogic 6.1)   */
        applicationConfigs = configReader.read();
    }

    public static ApplicationConfig getApplicationConfig(String applicationId){

        for(Iterator it=applicationConfigs.iterator(); it.hasNext(); ){
            ApplicationConfig appConfig = (ApplicationConfig)it.next();
            if(appConfig.getApplicationId().equals(applicationId)){
                return appConfig;
            }
            if(appConfig.isCluster()){
                for(Iterator it2=appConfig.getApplications().iterator(); it2.hasNext();){
                    appConfig = (ApplicationConfig)it2.next();
                    if(appConfig.getApplicationId().equals(applicationId)){
                        return appConfig;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Retrieve all configured applications.
     *
     * @return
     */
    public static List getApplications(){
        return applicationConfigs;
    }

    public static void addApplication(ApplicationConfig config){
        applicationConfigs.add(config);
        saveConfig();
    }

    public static void updateApplication(ApplicationConfig config) {
        int index = applicationConfigs.indexOf(config);
        applicationConfigs.remove(index);
        applicationConfigs.add(index, config);
        saveConfig();
    }

    public static void deleteApplication(String applicationId) {

        assert applicationId != null: "applicationId is null";
        ApplicationConfig config = getApplicationConfig(applicationId);
        assert config != null: "there is no application with id="+applicationId;
        applicationConfigs.remove(config);
        saveConfig();
    }

    private static void saveConfig(){
        ConfigWriter writer = ConfigWriter.getInstance();
        writer.write(applicationConfigs);
    }
}
