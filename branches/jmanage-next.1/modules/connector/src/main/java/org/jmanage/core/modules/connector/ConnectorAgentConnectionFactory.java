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
package org.jmanage.core.modules.connector;

import org.jmanage.core.management.ServerConnectionFactory;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.connector.framework.ConnectorRegistry;

import javax.management.MBeanServerConnection;

/**
 * @author	Tak-Sang Chan
 */
public class ConnectorAgentConnectionFactory implements ServerConnectionFactory {

    public ServerConnection getServerConnection(ApplicationConfig config)
            throws ConnectionFailedException {

        try {
            MBeanServerConnection mbsc = ConnectorRegistry.getInstance(config).getMBeanServer();
            return new ConnectorAgentConnection(mbsc);
        } catch (Throwable ex) {
            throw new ConnectionFailedException(ex);
        }
    }
}


