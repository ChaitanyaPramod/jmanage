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

import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ObjectInfo;
import org.jmanage.core.data.MBeanData;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.MBeanConfig;

import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.List;

/**
 *
 * date:  Feb 21, 2005
 * @author	Rakesh Kalra
 */
public class MBeanServiceImpl implements MBeanService {

    private static final String DEFAULT_FILTER = "*:*";

    public List getMBeans(ServiceContext context,
                          String applicationName,
                          String filter)
            throws ServiceException {

        ServerConnection serverConnection =
                ServiceUtils.getServerConnection(applicationName);

        if(filter == null){
            filter = DEFAULT_FILTER;
        }
        Set mbeans =
                serverConnection.queryNames(new ObjectName(filter));
        ArrayList mbeanDataList = new ArrayList(mbeans.size());
        for(Iterator it=mbeans.iterator();it.hasNext(); ){
            ObjectName objName = (ObjectName)it.next();
            mbeanDataList.add(new MBeanData(objName.getCanonicalName()));
        }
        return mbeanDataList;
    }

    public ObjectInfo getMBean(ServiceContext context,
                               String appName,
                               String mbeanName)
            throws ServiceException {

        ServerConnection serverConnection =
                ServiceUtils.getServerConnection(appName);
        mbeanName = resolveMBeanName(appName, mbeanName);
        ObjectInfo objectInfo =
                serverConnection.getObjectInfo(new ObjectName(mbeanName));
        return objectInfo;
    }

    /**
     * @return list of attribute values for given attributes
     */
    public List getAttributes(ServiceContext context,
                              String appName,
                              String mbeanName,
                              String[] attributes)
            throws ServiceException {
        ServerConnection connection =
                ServiceUtils.getServerConnection(appName);
        mbeanName = resolveMBeanName(appName, mbeanName);
        return connection.getAttributes(new ObjectName(mbeanName), attributes);
    }

    private String resolveMBeanName(String appName, String mbeanName){
        ApplicationConfig appConfig =
                ApplicationConfigManager.getApplicationConfigByName(appName);
        /* check if the mbeanName is the configured mbean name */
        MBeanConfig mbeanConfig = appConfig.findMBean(mbeanName);
        if(mbeanConfig != null){
            mbeanName = mbeanConfig.getObjectName();
        }
        return mbeanName;
    }
}
