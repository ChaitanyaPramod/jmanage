package org.jmanage.modules.jsr160;

import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.config.ApplicationConfig;

import javax.management.remote.JMXServiceURL;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.MBeanServerConnection;
import java.util.HashMap;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class JSR160ServerConnectionFactory implements
        org.jmanage.core.management.ServerConnectionFactory{

    /**
     * @return  instance of ServerConnection corresponding to this weblogic
     *              module.
     */
    public ServerConnection getServerConnection(ApplicationConfig config)
        throws ConnectionFailedException {

        try {
            /* Create an RMI connector client */
            HashMap env = new HashMap();
            String[] credentials = new String[] {config.getUsername(),
                                                 config.getPassword()};
            env.put("jmx.remote.credentials", credentials);
            JMXServiceURL url = new JMXServiceURL(config.getURL());
            JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
            return new JSR160ServerConnection(mbsc);
        } catch (Throwable e) {
            throw new ConnectionFailedException(e);
        }
    }
}
