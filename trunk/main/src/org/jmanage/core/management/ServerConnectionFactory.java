package org.jmanage.core.management;

import org.jmanage.core.config.ApplicationConfig;

/**
 * Factory class used for creating ServerConnection instance. Every module
 * must implement this interface.
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public interface ServerConnectionFactory {

    /**
     * @return  instance of ServerConnection corresponding to the module.
     */
    public ServerConnection getServerConnection(ApplicationConfig config)
            throws ConnectionFailedException;
}
