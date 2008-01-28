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
import org.jmanage.core.data.ApplicationConfigData;
import org.jmanage.core.data.MBeanData;
import org.jmanage.core.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * date:  Jan 17, 2005
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class ConfigurationServiceImpl implements ConfigurationService {

    @SuppressWarnings("unused")
    private static Logger logger = Loggers.getLogger(ConfigurationService.class);

    /**
     * Add/Configure a new application without any dashboards.
     *
     * @param context
     * @param data
     * @return Application config with dashboard details.
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
        for (Object appConfig1 : appConfigs) {
            ApplicationConfigData configData = new ApplicationConfigData();
            ApplicationConfig appConfig = (ApplicationConfig) appConfig1;
            CoreUtils.copyProperties(configData, appConfig);
            appDataObjs.add(configData);
            if (appConfig.isCluster()) {
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
        for (Object mbeanConfig1 : mbeanList) {
            MBeanConfig mbeanConfig = (MBeanConfig) mbeanConfig1;
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
}
