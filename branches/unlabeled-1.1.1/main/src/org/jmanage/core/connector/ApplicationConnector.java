package org.jmanage.core.connector;

import org.jmanage.core.config.ApplicationConfig;

import javax.management.MBeanServer;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public interface ApplicationConnector {

    public MBeanServer connect(ApplicationConfig config)
            throws ConnectionFailedException ;
}
