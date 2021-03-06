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
package org.jmanage.core.modules.jboss;

import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.management.ServerConnectionFactory;
import org.jmanage.core.config.ApplicationConfig;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;

import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.Hashtable;

/**
 *
 * date:  Oct 30, 2004
 * @author	Prem, Shashank Bellary
 */
public class JBossServerConnectionFactory implements ServerConnectionFactory{

    /**
     * @return  instance of ServerConnection corresponding to this jboss
     * module.
     */
    public ServerConnection getServerConnection(ApplicationConfig config)
        throws ConnectionFailedException {

        try {
            RMIAdaptor rmiAdaptor = findExternal(config.getURL(),
                    config.getUsername(), config.getPassword());
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
    private static RMIAdaptor findExternal(String url, String username, String password)
            throws NamingException {

        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        props.put(Context.PROVIDER_URL, url);
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");

        if(username != null){
            SecurityAssociation.setPrincipal(new SimplePrincipal(username));
            SecurityAssociation.setCredential(password);
        }

        // As of 3.2.2 version of JBoss "jmx/invoker/RMIAdaptor" is the new JNDI entry for RMIAdaptor.
        ///  But there is a naming alias setup from jmx/rmi/RMIAdaptor to jmx/invoker/RMIAdaptor,
        ///   so connectivity continues to work fine (last tested with 4.0.4GA).
        Context ctx = new InitialContext(props);
        RMIAdaptor rmiAdaptor = (RMIAdaptor)ctx.lookup("jmx/rmi/RMIAdaptor"); 
        ctx.close();
        return rmiAdaptor;
    }

}
