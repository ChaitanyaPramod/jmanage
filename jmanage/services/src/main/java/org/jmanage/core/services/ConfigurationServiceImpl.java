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
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.ACLConstants;
import org.jmanage.core.util.ErrorCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 *
 * date:  Jan 17, 2005
 * @author	Rakesh Kalra
 */
public class ConfigurationServiceImpl implements ConfigurationService {

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
                        null);

        try {
            ApplicationConfigManager.addApplication(config);
        } catch (ApplicationConfigManager.DuplicateApplicationNameException e) {
            throw new ServiceException(ErrorCodes.APPLICATION_NAME_ALREADY_EXISTS,
                    e.getAppName());
        }

        data.setApplicationId(appId);

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
        ArrayList appDataObjs = new ArrayList(appConfigs.size());
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


    public List getConfiguredMBeans(ServiceContext context)
            throws ServiceException {

        AccessController.checkAccess(context,
                ACLConstants.ACL_VIEW_APPLICATIONS);

        String applicationName = context.getApplicationConfig().getName();
        ApplicationConfig appConfig =
                ServiceUtils.getApplicationConfigByName(applicationName);
        List mbeanList = appConfig.getMBeans();
        ArrayList mbeanDataList = new ArrayList(mbeanList.size());
        for(Iterator it=mbeanList.iterator(); it.hasNext();){
            MBeanConfig mbeanConfig = (MBeanConfig)it.next();
            mbeanDataList.add(new MBeanData(mbeanConfig.getObjectName(),
                    mbeanConfig.getName()));
        }
        return mbeanDataList;
    }

    public GraphConfig addGraph(ServiceContext context,GraphConfig graphConfig){
        ApplicationConfig appConfig = context.getApplicationConfig();
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
