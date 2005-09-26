/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.core.management;

import org.jmanage.core.config.*;
import org.jmanage.core.util.Loggers;

import java.lang.reflect.Proxy;
import java.util.logging.Logger;

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

        ApplicationType appType =
                ApplicationTypes.getApplicationType(appConfig.getType());
        assert appType != null: "Invalid type=" + appConfig.getType();
        ModuleConfig moduleConfig = appType.getModule();
        final ClassLoader classLoader = appType.getClassLoader();
        assert classLoader != null;

        final ClassLoader contextClassLoader =
                Thread.currentThread().getContextClassLoader();
        /* temporarily change the thread context classloader */
        Thread.currentThread().setContextClassLoader(classLoader);

        try {
            logger.fine("Connecting to " + appConfig.getURL());
            final ServerConnectionFactory factory =
                    getServerConnectionFactory(moduleConfig, classLoader);
            ServerConnection connection =
                    factory.getServerConnection(appConfig);
            logger.fine("Connected to " + appConfig.getURL());
            ServerConnectionProxy proxy = new ServerConnectionProxy(connection,
                    classLoader);
            return (ServerConnection)Proxy.newProxyInstance(
                    ServerConnector.class.getClassLoader(),
                    new Class[]{ServerConnection.class},
                    proxy);
        } catch(ConnectionFailedException e){
            logger.info("Failed to connect. error=" + e.getMessage());
            throw e;
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
