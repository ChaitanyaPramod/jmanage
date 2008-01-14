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
package org.jmanage.core.modules.weblogic;

import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.management.ServerConnectionFactory;
import org.jmanage.core.config.ApplicationConfig;
import weblogic.management.MBeanHome;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class WLServerConnectionFactory implements ServerConnectionFactory{

    /**
     * @return  instance of ServerConnection corresponding to this weblogic
     *              module.
     */
    public ServerConnection getServerConnection(ApplicationConfig config)
        throws ConnectionFailedException {

        try {
            Hashtable<String, Object> props = new Hashtable<String, Object>();
            props.put(Context.INITIAL_CONTEXT_FACTORY,
                    "weblogic.jndi.WLInitialContextFactory");
            props.put(Context.PROVIDER_URL,         config.getURL());
            props.put(Context.SECURITY_PRINCIPAL,   config.getUsername());
            props.put(Context.SECURITY_CREDENTIALS, config.getPassword());
            Context ctx =  new InitialContext(props);
            MBeanHome home = (MBeanHome) ctx.lookup(MBeanHome.JNDI_NAME + "." +
                            "localhome");
            // Fix for 1586075: Passing context in, so that it can be closed when the connection
            //   is closed
            return new WLServerConnection(home.getMBeanServer(), ctx);
        } catch (Throwable e) {
            throw new ConnectionFailedException(e);
        }
    }
}
