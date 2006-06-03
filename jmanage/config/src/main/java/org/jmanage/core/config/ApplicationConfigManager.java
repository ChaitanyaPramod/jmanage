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
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class ApplicationConfigManager {

    private static List<ApplicationConfig> applicationConfigs =
            Collections.synchronizedList(new LinkedList<ApplicationConfig>());

    private static final ConfigReader configReader = ConfigReader.getInstance();

    static{
        Config config = configReader.read();
        /* read the configuration */
        applicationConfigs.addAll(config.getApplications());
        /* create a backup of configuration file */
        new File(ConfigConstants.DEFAULT_CONFIG_FILE_NAME).renameTo(
                new File(ConfigConstants.BOOTED_CONFIG_FILE_NAME));
        /* write from memory */
        ConfigWriter.getInstance().write(config);
    }

    public static ApplicationConfig getApplicationConfigByName(String appName){

        for(Iterator it=applicationConfigs.iterator(); it.hasNext(); ){
            ApplicationConfig appConfig = (ApplicationConfig)it.next();
            if(appConfig.getName().equals(appName)){
                return appConfig;
            }
            if(appConfig.isCluster()){
                for(Iterator it2=appConfig.getApplications().iterator(); it2.hasNext();){
                    appConfig = (ApplicationConfig)it2.next();
                    if(appConfig.getName().equals(appName)){
                        return appConfig;
                    }
                }
            }
        }
        return null;
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
    public static List<ApplicationConfig> getApplications(){
        return applicationConfigs;
    }

    private static final Object writeLock = new Object();

    public static void addApplication(ApplicationConfig config)
        throws DuplicateApplicationNameException {

        synchronized(writeLock){
            // validate the application name
            validateAppName(config.getName(), null);
            applicationConfigs.add(config);
            saveConfig();
        }
    }

    public static void updateApplication(ApplicationConfig config)
        throws DuplicateApplicationNameException {

        assert config != null: "application config is null";

        synchronized(writeLock){
            // validate the application name
            validateAppName(config.getName(), config.getApplicationId());

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
        writer.write(new Config(applicationConfigs));
    }

    public static List getAllApplications(){
        Iterator appItr = applicationConfigs.iterator();
        List<ApplicationConfig> applications = new LinkedList<ApplicationConfig>();
        while(appItr.hasNext()){
            ApplicationConfig appConfig = (ApplicationConfig)appItr.next();
            applications.add(appConfig);
            if(appConfig.isCluster()){
                applications.addAll(appConfig.getApplications());
            }
        }
        return applications;
    }

    public static List<AlertConfig> getAllAlerts() {
        Iterator appItr = applicationConfigs.iterator();
        List<AlertConfig> alerts = new LinkedList<AlertConfig>();
        while(appItr.hasNext()){
            ApplicationConfig appConfig = (ApplicationConfig)appItr.next();
            alerts.addAll(appConfig.getAlerts());
            if(appConfig.isCluster()){
                for(Iterator it=appConfig.getApplications().iterator();
                    it.hasNext();){
                    ApplicationConfig childAppConfig =
                            (ApplicationConfig)it.next();
                    alerts.addAll(childAppConfig.getAlerts());
                }
            }
        }
        return alerts;
    }

    private static void validateAppName(String appName, String applicationId)
        throws DuplicateApplicationNameException {
        for(Iterator it=getApplications().iterator(); it.hasNext(); ){
            ApplicationConfig appConfig = (ApplicationConfig)it.next();
            if(!appConfig.getApplicationId().equals(applicationId) &&
                    (appConfig.getName().toUpperCase()).equals(appName.toUpperCase())) {
                throw new DuplicateApplicationNameException(appName);
            }
        }
    }

    public static class DuplicateApplicationNameException extends Exception{

    	private static final long serialVersionUID = -718362762247140502L;
		
    	// app name that is duplicate
        private final String appName;

        public DuplicateApplicationNameException(String appName){
            super("Application name: " + appName);
            this.appName = appName;
        }

        public String getAppName(){
            return appName;
        }
    }
}
