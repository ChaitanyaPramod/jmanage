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
import org.jmanage.core.management.*;
import org.jmanage.core.services.ServiceUtils;
import org.jmanage.core.util.Loggers;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: Jun 21, 2006 7:33:11 PM
 *
 * @author Shashank Bellary
 */
public class GenericMBeanCheckQualifier extends BaseDashboardQualifier {
    private static final Logger logger =
            Loggers.getLogger(GenericMBeanCheckQualifier.class);
    private ObjectName objectName;

    protected void init(Map<String, String> properties) {
        try {
            objectName = new ObjectName(properties.get(MBEAN));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isQualified(ApplicationConfig applicationConfig) {
        ServerConnection serverConnection = null;
        try {
            serverConnection =
                    ServerConnector.getServerConnection(applicationConfig);
            ObjectInfo objectInfo = serverConnection.getObjectInfo(objectName);
            return objectInfo != null;
        } catch (Exception e){
            logger.log(Level.FINE, new StringBuilder().append(
                    "Error finding mbean "+objectName+" for:").append(
                    applicationConfig.getName()).toString(), e);
        } finally{
            ServiceUtils.close(serverConnection);
        }
        return false;
    }
}
