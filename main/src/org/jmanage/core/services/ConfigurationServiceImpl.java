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

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigFactory;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.MBeanConfig;
import org.jmanage.core.data.ApplicationConfigData;
import org.jmanage.core.data.MBeanData;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.util.CoreUtils;

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

        /* TODO: first check the permission */

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

        ApplicationConfigManager.addApplication(config);
        data.setApplicationId(appId);

        /* log the operation */
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Added application "+ "\""+config.getName()+"\"");

        return data;
    }

    public List getAllApplications(ServiceContext context) {
        List appConfigs = ApplicationConfigManager.getApplications();
        ArrayList appDataObjs = new ArrayList(appConfigs.size());
        for(Iterator it=appConfigs.iterator(); it.hasNext(); ){
            ApplicationConfigData configData = new ApplicationConfigData();
            CoreUtils.copyProperties(configData, it.next());
            appDataObjs.add(configData);
        }
        return appDataObjs;
    }

    public List getConfiguredMBeans(ServiceContext context,
                                    String applicationName)
            throws ServiceException {
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
}
