package org.jmanage.modules.tomcat;

import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.config.ApplicationConfig;
import org.apache.commons.modeler.Registry;

import javax.management.MBeanServer;

/**
 * Date: Aug 31, 2004 10:23:59 PM
 * @author Shashank Bellary 
 */
public class TomcatServerConnectionFactory implements
        org.jmanage.core.management.ServerConnectionFactory{

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
