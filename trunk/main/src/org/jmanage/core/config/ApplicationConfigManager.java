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
        assert config != null: "application config is null";
        int index = applicationConfigs.indexOf(config);
        if(index != -1){
            applicationConfigs.remove(index);
            applicationConfigs.add(index, config);
        }else{
            /* its part of a cluster */
            assert config.isCluster() == false;
            ApplicationConfig clusterConfig = config.getClusterConfig();
            assert clusterConfig != null;
            index = clusterConfig.getApplications().indexOf(config);
            assert index != -1: "application not found in cluster";
            clusterConfig.getApplications().remove(index);
            clusterConfig.getApplications().add(index, config);
        }
        saveConfig();
    }

    public static ApplicationConfig deleteApplication(String applicationId) {
        assert applicationId != null: "applicationId is null";
        ApplicationConfig config = getApplicationConfig(applicationId);
        assert config != null: "there is no application with id="+applicationId;
        deleteApplication(config);
        return(config);
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