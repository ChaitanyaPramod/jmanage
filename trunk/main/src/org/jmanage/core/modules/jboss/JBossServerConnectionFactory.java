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
package org.jmanage.modules.jboss;

import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.config.ApplicationConfig;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;

import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.Hashtable;

/**
 *
 * date:  Oct 30, 2004
 * @author	Prem
 */
public class JBossServerConnectionFactory implements
        org.jmanage.core.management.ServerConnectionFactory{

    /**
     * @return  instance of ServerConnection corresponding to this jboss
     * module.
     */
    public ServerConnection getServerConnection(ApplicationConfig config)
        throws ConnectionFailedException {

        try {
            RMIAdaptor rmiAdaptor = findExternal(config.getURL());
            return new JBossServerConnection(rmiAdaptor);
        } catch (Throwable e) {
            throw new ConnectionFailedException(e);
        }
    }

    /**
     *
     * @param url
     * @return
     * @throws NamingException
     */
    private static RMIAdaptor findExternal(String url)
            throws NamingException {

        Hashtable props = new Hashtable();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        props.put(Context.PROVIDER_URL, url);
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
        Context ctx = new InitialContext(props);
        RMIAdaptor rmiAdaptor = (RMIAdaptor)ctx.lookup("jmx/rmi/RMIAdaptor");
        ctx.close();
        return rmiAdaptor;
    }

}
