package org.jmanage.modules.weblogic;

import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.WeblogicApplicationConfig;
import weblogic.management.MBeanHome;
import weblogic.jndi.Environment;

import javax.naming.NamingException;
import javax.naming.Context;

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


        WeblogicApplicationConfig wlConfig = (WeblogicApplicationConfig)config;
        try {
            MBeanHome home = findExternal(config.getURL(), config.getUsername(),
                    config.getPassword());
            return new WLServerConnection(home.getMBeanServer());
        } catch (NamingException e) {
            throw new ConnectionFailedException(e);
        }
    }

    private static MBeanHome findExternal(String url,
                                          String username,
                                          String password)
            throws NamingException {

        Environment env = new Environment();
        env.setProviderUrl(url);
        env.setSecurityPrincipal(username);
        env.setSecurityCredentials(password);

        Context ctx = env.getInitialContext();
        MBeanHome home = (MBeanHome) ctx.lookup(MBeanHome.JNDI_NAME + "." +
                "localhome");
        ctx.close();
        return home;
    }
}
