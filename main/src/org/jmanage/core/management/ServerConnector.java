package org.jmanage.core.management;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.modules.ModuleConfig;
import org.jmanage.core.modules.ModuleRegistry;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.Loggers;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.net.URLClassLoader;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class ServerConnector {

    private static final Logger logger =
            Loggers.getLogger(ServerConnector.class);

    /**
     * Returns a ServerConnection for the given application config.
     *
     * @param appConfig
     * @return
     * @throws ConnectionFailedException
     */
    public static ServerConnection
            getServerConnection(ApplicationConfig appConfig)
        throws ConnectionFailedException{

        ModuleConfig moduleConfig =
                    ModuleRegistry.getModule(appConfig.getType());
        assert moduleConfig != null: "Invalid type=" + appConfig.getType();
        final ClassLoader classLoader = moduleConfig.getClassLoader();
        assert classLoader != null;

        final ClassLoader contextClassLoader =
                Thread.currentThread().getContextClassLoader();
        /* temporarily change the thread context classloader */
        Thread.currentThread().setContextClassLoader(classLoader);

        try {
            final ServerConnectionFactory factory =
                    getServerConnectionFactory(moduleConfig, classLoader);
            ServerConnection connection =
                    factory.getServerConnection(appConfig);
            return new ServerConnectionProxy(connection, classLoader);
        } finally {
            /* change the thread context classloader back to the
                    original classloader*/
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    // TODO: we should cache the factory instance
    private static ServerConnectionFactory
            getServerConnectionFactory(ModuleConfig moduleConfig,
                                       ClassLoader classLoader) {

        try {
            assert classLoader != null;
            final Class factoryClass =
                    Class.forName(moduleConfig.getConnectionFactory(),
                            true, classLoader);
            return (ServerConnectionFactory)factoryClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
