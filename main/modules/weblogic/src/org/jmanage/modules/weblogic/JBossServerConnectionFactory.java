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
            RMIAdaptor rmiAdaptor = findExternal(config.getURL(), config.getUsername(),
                    config.getPassword());
            return new JBossServerConnection(rmiAdaptor);
        } catch (Throwable e) {
            throw new ConnectionFailedException(e);
        }
    }

    private static RMIAdaptor findExternal(String url,
                                          String username,
                                          String password)
            throws NamingException {

        Hashtable props = new Hashtable();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        props.put(Context.PROVIDER_URL,         url);
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
        props.put(Context.SECURITY_PRINCIPAL,   username);
        props.put(Context.SECURITY_CREDENTIALS, password);
        Context ctx =  new InitialContext(props);
        RMIAdaptor rmiAdaptor = (RMIAdaptor) ctx.lookup("jmx/rmi/RMIAdaptor");
        ctx.close();
        return rmiAdaptor;
    }

}
