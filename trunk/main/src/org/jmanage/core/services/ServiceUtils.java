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
import org.jmanage.core.management.ServerConnector;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.util.ErrorCodes;

/**
 *
 * date:  Feb 21, 2005
 * @author	Rakesh Kalra
 */
public class ServiceUtils {

    public static ServerConnection getServerConnection(String appName){
        ApplicationConfig appConfig = getApplicationConfigByName(appName);
        if(appConfig.isCluster()){
            throw new ServiceException(
                    ErrorCodes.OPERATION_NOT_SUPPORTED_FOR_CLUSTER);
        }
        return ServerConnector.getServerConnection(appConfig);
    }

    public static ApplicationConfig getApplicationConfigByName(String appName)
        throws ServiceException {

        ApplicationConfig appConfig =
                ApplicationConfigManager.getApplicationConfigByName(appName);
        if(appConfig == null){
            throw new ServiceException(
                    ErrorCodes.INVALID_APPLICATION_NAME, appName);
        }
        return appConfig;
    }

}
