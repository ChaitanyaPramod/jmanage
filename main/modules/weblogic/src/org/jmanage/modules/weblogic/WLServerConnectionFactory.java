package org.jmanage.modules.weblogic;

import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.config.ApplicationConfig;
import weblogic.management.MBeanHome;
import weblogic.jndi.WLInitialContextFactory;
import weblogic.jndi.Environment;

import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.spi.InitialContextFactoryBuilder;
import java.util.Hashtable;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class WLServerConnectionFactory implements
        org.jmanage.core.management.ServerConnectionFactory{

    /**
     * @return  instance of ServerConnection corresponding to this weblogic
     *              module.
     */
    public ServerConnection getServerConnection(ApplicationConfig config)
        throws ConnectionFailedException {

        try {
            MBeanHome home = findExternal(config.getURL(), config.getUsername(),
                    config.getPassword());
            return new WLServerConnection(home.getMBeanServer());
        } catch (Throwable e) {
            throw new ConnectionFailedException(e);
        }
    }

    private static MBeanHome findExternal(String url,
                                          String username,
                                          String password)
            throws NamingException {

        Hashtable props = new Hashtable();
        props.put(Context.INITIAL_CONTEXT_FACTORY,
                "weblogic.jndi.WLInitialContextFactory");
        props.put(Context.PROVIDER_URL,         url);
        props.put(Context.SECURITY_PRINCIPAL,   username);
        props.put(Context.SECURITY_CREDENTIALS, password);
        Context ctx =  new InitialContext(props);
        MBeanHome home = (MBeanHome) ctx.lookup(MBeanHome.JNDI_NAME + "." +
                        "localhome");
        ctx.close();
        return home;
    }

}
