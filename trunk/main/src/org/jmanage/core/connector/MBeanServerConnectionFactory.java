package org.jmanage.core.connector;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.util.Tracer;

import javax.management.MBeanServer;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class MBeanServerConnectionFactory {

    /* Known connectors */
    private static final Map connectors = new HashMap();
    static{
        connectors.put(ApplicationConfig.TYPE_WEBLOGIC,
                new WeblogicConnector());
    }

    public static MBeanServer getConnection(ApplicationConfig config)
        throws ConnectionFailedException {

        ApplicationConnector connector = getConnector(config);
        return connector.connect(config);
    }

    private static ApplicationConnector getConnector(ApplicationConfig config){
        ApplicationConnector connector =
                (ApplicationConnector)connectors.get(config.getType());
        if(connector == null){
            throw new RuntimeException("Invalid application type: "
                    + config.getType());
        }
        return connector;
    }
}
