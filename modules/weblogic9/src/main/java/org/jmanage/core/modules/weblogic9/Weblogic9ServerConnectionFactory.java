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
package org.jmanage.core.modules.weblogic9;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ServerConnectionFactory;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.util.Hashtable;

/**
 *
 * date:  Mar 23, 2006
 * @author	Rakesh Kalra
 */
public class Weblogic9ServerConnectionFactory implements ServerConnectionFactory {

    /**
     * @return  instance of ServerConnection corresponding to this jsr160
     *              module.
     */
    public ServerConnection getServerConnection(ApplicationConfig config)
            throws ConnectionFailedException {

        try {
            JMXServiceURL url = new JMXServiceURL(config.getURL());

            Hashtable env = new Hashtable();
            env.put(Context.SECURITY_PRINCIPAL, config.getUsername());
            env.put(Context.SECURITY_CREDENTIALS, config.getPassword());
            env.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
                    "weblogic.management.remote");
            JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
            return new Weblogic9ServerConnection(jmxc, jmxc.getMBeanServerConnection());
        } catch (Throwable e) {
            throw new ConnectionFailedException(e);
        }
    }
}
