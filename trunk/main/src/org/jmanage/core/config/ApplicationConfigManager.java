package org.jmanage.core.config;

import java.util.*;
import java.io.File;

/**
 *
 * date:  Jun 13, 2004
 * @author	Rakesh Kalra
 */
public class ApplicationConfigManager{

    private static List applicationConfigs = null;

    private static final ConfigReader configReader = ConfigReader.getInstance();

    static{
        /* read the configuration */
        applicationConfigs = configReader.read();
        /* create a backup of configuration file */
        new File(ConfigConstants.DEFAULT_CONFIG_FILE_NAME).renameTo(
                new File(ConfigConstants.BOOTED_CONFIG_FILE_NAME));
        /* write from memory */
        ConfigWriter.getInstance().write(applicationConfigs);
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
        deleteApplication(config);
    }

    public static void deleteApplication(ApplicationConfig config) {
        assert config != null: "application config is null";
        if(!applicationConfigs.remove(config)){
            /* this app is in a cluster. remove from cluster */
            for(Iterator it=applicationConfigs.iterator(); it.hasNext(); ){
                ApplicationConfig appConfig = (ApplicationConfig)it.next();
                if(appConfig.isCluster()){
                    ApplicationClusterConfig clusterConfig =
                            (ApplicationClusterConfig)appConfig;
                    if(clusterConfig.removeApplication(config)){
                        break;
                    }
                }
            }
        }
        saveConfig();
    }

    private static void saveConfig(){
        ConfigWriter writer = ConfigWriter.getInstance();
        writer.write(applicationConfigs);
    }
}
