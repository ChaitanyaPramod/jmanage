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

package org.jmanage.core.modules.websphere;

import org.jmanage.core.management.ServerConnectionFactory;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.config.ApplicationConfig;

import java.util.Properties;

import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.AdminClientFactory;

/**
 * Date: Jan 23, 2005 5:48:07 PM
 * @author Shashank Bellary 
 */
public class WebSphereServerConnectionFactory implements ServerConnectionFactory{
    /**
     * Get connection to the server.
     *
     * @param config
     * @return
     * @throws ConnectionFailedException
     */
    public ServerConnection getServerConnection(ApplicationConfig config)
            throws ConnectionFailedException {
        /*    Initialize the AdminClient  */
        Properties adminProps = new Properties();
        adminProps.setProperty(AdminClient.CONNECTOR_TYPE,
              AdminClient.CONNECTOR_TYPE_SOAP);
        adminProps.setProperty(AdminClient.CONNECTOR_HOST, config.getHost());
        adminProps.setProperty(AdminClient.CONNECTOR_PORT,
              config.getPort().toString());
        if(config.getUsername() != null && config.getUsername().trim().length() > 0){
            adminProps.setProperty(AdminClient.USERNAME, config.getUsername());
            adminProps.setProperty(AdminClient.PASSWORD, config.getPassword());
        }
        try{
            AdminClient adminClient = AdminClientFactory.createAdminClient(adminProps);
            return new WebSphereServerConnection(adminClient);
        }catch(Throwable e){
            throw new ConnectionFailedException(e);
        }
    }
}
