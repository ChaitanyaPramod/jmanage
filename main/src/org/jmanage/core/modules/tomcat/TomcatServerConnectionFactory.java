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
package org.jmanage.core.modules.tomcat;

import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.management.ServerConnectionFactory;
import org.jmanage.core.config.ApplicationConfig;
import org.apache.commons.modeler.Registry;

import javax.management.MBeanServer;

/**
 * Date: Aug 31, 2004 10:23:59 PM
 * @author Shashank Bellary 
 */
public class TomcatServerConnectionFactory implements ServerConnectionFactory{

    public ServerConnection getServerConnection(ApplicationConfig config)
            throws ConnectionFailedException {
        try {
            /*  Though the methods are depricated, currently this is the best
                way to get hold of MBeanServer. */
            MBeanServer mBeanServer = Registry.getRegistry().getServer();
            return new TomcatServerConnection(mBeanServer);
        } catch (Throwable e) {
            throw new ConnectionFailedException(e);
        }
    }
}
