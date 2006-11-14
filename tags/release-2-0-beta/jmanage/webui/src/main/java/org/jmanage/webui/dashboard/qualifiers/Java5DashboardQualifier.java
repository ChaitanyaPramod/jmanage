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

import org.jmanage.webui.dashboard.framework.BaseDashboardQualifier;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ServerConnector;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.services.ServiceUtils;

import javax.management.InstanceNotFoundException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Map;

/**
 * Date: Jun 18, 2006 4:08:21 PM
 *
 * @author Shashank Bellary
 */
public class Java5DashboardQualifier extends BaseDashboardQualifier {
    private static final Logger logger =
            Loggers.getLogger(Java5DashboardQualifier.class);
    private ObjectName objectName;
    private String attributeName;

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
            String value = (String)serverConnection.getAttribute(objectName,
                    attributeName);
            return value.startsWith("1.5");
        } catch (ConnectionFailedException e){
            logger.log(Level.FINE, new StringBuilder().append(
                    "Error retrieving attributes for:").append(
                    applicationConfig.getName()).toString(), e);
        } catch(Exception e){
            if(e instanceof InstanceNotFoundException ||
                    e.getCause() instanceof InstanceNotFoundException){
                logger.log(Level.INFO,
                        "Specified object name/ attribute not found");
            }else{
                logger.log(Level.SEVERE, "Unknown error", e);
            }
        } finally{
            ServiceUtils.close(serverConnection);
        }
        return false;
    }

    protected void init(Map<String, String> properties) {
        try {
            objectName = new ObjectName(properties.get(MBEAN));
            attributeName = properties.get(ATTRIBUTE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}