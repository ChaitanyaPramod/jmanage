package org.jmanage.core.connector;

import org.jmanage.core.config.WeblogicApplicationConfig;
import org.jmanage.core.config.ApplicationConfig;

import javax.management.MBeanServer;
import javax.naming.Context;
import javax.naming.NamingException;

import weblogic.management.MBeanHome;
import weblogic.jndi.Environment;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class WeblogicConnector implements ApplicationConnector {

    public MBeanServer connect(ApplicationConfig config)
        throws ConnectionFailedException{

        WeblogicApplicationConfig wlConfig = (WeblogicApplicationConfig)config;
        try {
            MBeanHome home = findExternal(config.getURL(), config.getUsername(),
                    config.getPassword());
            return home.getMBeanServer();
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
