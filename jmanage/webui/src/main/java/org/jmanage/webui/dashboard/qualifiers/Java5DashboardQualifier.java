/**
 * Copyright 2004-2006 jManage.org
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
package org.jmanage.webui.dashboard.qualifiers;

import org.jmanage.webui.dashboard.framework.DashboardQualifier;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ServerConnector;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.services.ServiceUtils;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Date: Jun 18, 2006 4:08:21 PM
 *
 * @ author: Shashank Bellary
 */
public class Java5DashboardQualifier implements DashboardQualifier {
    private static final Logger logger =
            Loggers.getLogger(Java5DashboardQualifier.class);
    private static final String JAVA5_QUALIFYING_MBEAN = "java.lang:type=Runtime";
    private static final String JAVA5_QUALIFYING_MBEAN_ATTRIBUTE = "VmVersion";

    /**
     * Java 5 dashboard qualifier used to associate a java5 dashboard with an
     * application.
     *
     * @param applicationConfig
     * @return boolean value indicating the qualifying status.
     */
    public boolean isQualified(ApplicationConfig applicationConfig) {

        ServerConnection serverConnection = null;
        try {
            serverConnection =
                    ServerConnector.getServerConnection(applicationConfig);
            String value = (String)serverConnection.getAttribute(new ObjectName(JAVA5_QUALIFYING_MBEAN),
                    JAVA5_QUALIFYING_MBEAN_ATTRIBUTE);
            return value.startsWith("1.5");
        } catch (ConnectionFailedException e){
            logger.log(Level.FINE, new StringBuilder().append(
                    "Error retrieving attributes for:").append(
                    applicationConfig.getName()).toString(), e);
        } finally{
            ServiceUtils.close(serverConnection);
        }
        return false;
    }
}