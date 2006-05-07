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
package org.jmanage.core.services;

import org.jmanage.core.config.*;
import org.jmanage.core.config.dashboard.framework.DashboardRepository;
import org.jmanage.core.data.ApplicationConfigData;
import org.jmanage.core.data.MBeanData;
import org.jmanage.core.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Logger;
import java.io.*;
import java.io.FileReader;

/**
 *
 * date:  Jan 17, 2005
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class ConfigurationServiceImpl implements ConfigurationService {
    private static DashboardRepository dashboardRepository =
            DashboardRepository.getInstance();
    private static Logger logger = Loggers.getLogger(ConfigurationService.class);

    /**
     * Check if there are any qualifying dashboards for the current application.
     *
     * @param context
     * @param data
     * @return
     */
    public ApplicationConfigData addAppWithDashboard(ServiceContext context,
                                                     ApplicationConfigData data){
        AccessController.checkAccess(context, ACLConstants.ACL_ADD_APPLICATIONS);
        /* do the operation */
        String appId = ApplicationConfig.getNextApplicationId();
        Integer port = data.getPort();

        ApplicationConfig config =
                ApplicationConfigFactory.create(appId, data.getName(),
                        data.getType(),
                        data.getHost(),
                        port,
                        data.getURL(),
                        data.getUsername(),
                        data.getPassword(),
                        data.getParamValues());
        addQualifiedDashboards(config);
        return addAppBasicDetails(context, data, config);
    }

    /**
     * Iterate through available dashboards and add any qualifying dashboards to
     * the current application being configured
     *
     * @param config
     */
    private void addQualifiedDashboards(ApplicationConfig config){
        List<DashboardConfig> qualifyingDashboards = new ArrayList<DashboardConfig>();
        for(DashboardConfig dashboardConfig : dashboardRepository.getAll()){
            for(DashboardQualifier qualifier : dashboardConfig.getQualifiers()){
                if(qualifier.isQualified(config)){
                    qualifyingDashboards.add(dashboardConfig);
                    break;
                }
            }
        }
        if(!qualifyingDashboards.isEmpty())
            config.setDashboards(qualifyingDashboards);
    }

    /**
     * Add/Configure a new application without any dashboards.
     * TODO is this really required?
     *
     * @param context
     * @param data
     * @return
     */
    public ApplicationConfigData addApplication(ServiceContext context,
                                                    ApplicationConfigData data){
        AccessController.checkAccess(context, ACLConstants.ACL_ADD_APPLICATIONS);
        /* do the operation */
        String appId = ApplicationConfig.getNextApplicationId();
        Integer port = data.getPort();

        ApplicationConfig config =
                ApplicationConfigFactory.create(appId, data.getName(),
                        data.getType(),
                        data.getHost(),
                        port,
                        data.getURL(),
                        data.getUsername(),
                        data.getPassword(),
                        data.getParamValues());
        return addAppBasicDetails(context, data, config);
    }

    /**
     * Save the current application on the config file.
     *
     * @param context
     * @param data
     * @param config
     * @return
     */
    public ApplicationConfigData addAppBasicDetails(ServiceContext context,
                                                    ApplicationConfigData data,
                                                    ApplicationConfig config){

        try {
            ApplicationConfigManager.addApplication(config);
        } catch (ApplicationConfigManager.DuplicateApplicationNameException e) {
            throw new ServiceException(ErrorCodes.APPLICATION_NAME_ALREADY_EXISTS,
                    e.getAppName());
        }

        data.setApplicationId(config.getApplicationId());

        /* log the operation */
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Added application "+ "\""+config.getName()+"\"");
        return data;
    }

    // TODO: sort the list before returning
    public List getAllApplications(ServiceContext context) {
        List appConfigs = ApplicationConfigManager.getApplications();
        return appConfigListToAppConfigDataList(appConfigs);
    }

    private List appConfigListToAppConfigDataList(List appConfigs){
        ArrayList<ApplicationConfigData> appDataObjs = 
        	new ArrayList<ApplicationConfigData>(appConfigs.size());
        for(Iterator it=appConfigs.iterator(); it.hasNext(); ){
            ApplicationConfigData configData = new ApplicationConfigData();
            ApplicationConfig appConfig = (ApplicationConfig)it.next();
            CoreUtils.copyProperties(configData, appConfig);
            appDataObjs.add(configData);
            if(appConfig.isCluster()){
                List childApplications =
                        appConfigListToAppConfigDataList(appConfig.getApplications());
                configData.setChildApplications(childApplications);
            }
        }
        return appDataObjs;
    }


    public List<MBeanData> getConfiguredMBeans(ServiceContext context)
            throws ServiceException {

        AccessController.checkAccess(context,
                ACLConstants.ACL_VIEW_APPLICATIONS);

        String applicationName = context.getApplicationConfig().getName();
        ApplicationConfig appConfig =
                ServiceUtils.getApplicationConfigByName(applicationName);
        List mbeanList = appConfig.getMBeans();
        ArrayList<MBeanData> mbeanDataList = new ArrayList<MBeanData>(mbeanList.size());
        for(Iterator it=mbeanList.iterator(); it.hasNext();){
            MBeanConfig mbeanConfig = (MBeanConfig)it.next();
            mbeanDataList.add(new MBeanData(mbeanConfig.getObjectName(),
                    mbeanConfig.getName()));
        }
        return mbeanDataList;
    }

    public GraphConfig addGraph(ServiceContext context,GraphConfig graphConfig){
        ApplicationConfig appConfig = context.getApplicationConfig();
        // todo: this method needs attention. GraphConfig is added before
        //   calling this method
        //appConfig.addGraph(graphConfig);
        try {
            ApplicationConfigManager.updateApplication(appConfig);
        } catch (ApplicationConfigManager.DuplicateApplicationNameException e) {
            // unexpected exception
            throw new RuntimeException(e);
        }
        return graphConfig;
    }


    public void addDashboard(ServiceContext context,
                             DashboardConfig config){

        AccessController.checkAccess(context, ACLConstants.ACL_ADD_DASHBOARD);

        /* first write the template to the disk */
        try {
            FileWriter writer =
                    new FileWriter(CoreUtils.getDashboardsDir() +
                    config.getDashboardId() + ".jsp");
            writer.write(config.getTemplate());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* now add the dashboard to the config.xml */
        ApplicationConfigManager.addDashboard(config);

        /* there is no need to keep the template in the memory */
        config.setTemplate(null);

        /* log the operation */
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Added Dashboard "+ "\""+config.getName()+"\"");
    }

    public void updateDashboard(ServiceContext context,
                                DashboardConfig config){

        AccessController.checkAccess(context, ACLConstants.ACL_EDIT_DASHBOARD);

        /* first write the template to the disk */
        try {
            FileWriter writer =
                    new FileWriter(CoreUtils.getDashboardsDir() +
                    config.getDashboardId() + ".jsp");
            writer.write(config.getTemplate());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* now add the dashboard to the config.xml */
        ApplicationConfigManager.updateDashboard(config);

        /* there is no need to keep the template in the memory */
        config.setTemplate(null);

        /* log the operation */
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Updated Dashboard "+ "\""+config.getName()+"\"");
    }

    public DashboardConfig getDashboard(ServiceContext context,
                                        String dashboardId){
        DashboardConfig config =
                ApplicationConfigManager.getDashboard(dashboardId);
        try {
            config.setTemplate(getTemplate(dashboardId));
        } catch (IOException e) {
            logger.severe("Dashboard template not found for id: " + dashboardId);
        }
        return config;
    }


    private String getTemplate(String dashboardId) throws IOException {

        StringBuffer template = new StringBuffer();
        FileReader freader = new FileReader(CoreUtils.getDashboardsDir() +
                dashboardId + ".jsp");
        BufferedReader reader = new BufferedReader(freader);
        String line = reader.readLine();
        while(line != null){
            template.append(line);
            template.append("\n");
            line = reader.readLine();
        }
        return template.toString();
    }
}
