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
package org.jmanage.testapp.jsr160;

import org.jmanage.core.util.Loggers;
import org.jmanage.testapp.mbeans.*;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.MBeanServerFactory;
import javax.management.openmbean.OpenDataException;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXAuthenticator;
import javax.security.auth.Subject;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

/**
 *
 * date:  Dec 19, 2004
 * @author	Rakesh Kalra
 */
public class JMXHelper {

    private static final Logger logger = Loggers.getLogger(JMXHelper.class);

    // the mbeanServer used by the application
    private static MBeanServer mbeanServer;

    // the lock used to obtain the first mbeanServer
    private static Object mbeanServerLock = new Object();

    /**
     * Obtains a reference to the MBean server. If at least one
     * MBean server already exists, then a reference to that MBean
     * server is returned. Otherwise a new instance is created.
     */
    private static MBeanServer getMBeanServer() {

        if (mbeanServer == null) {
            mbeanServer = _getMBeanServer();
        }
        return mbeanServer;
    }

    /**
     * registers the MBeans used in the application
     * and starts the HTML Adaptor
     */
    public static void registerMBeans(int port, boolean jmxmpConnector) {

        /* Configuration */
        registerMBean(new Configuration(), ObjectNames.CONFIGURATION);
        /* Calculator */
        registerMBean(new Calculator(), ObjectNames.CALCULATOR);
        /* PrimitiveDataTypeTest */
        registerMBean(new PrimitiveDataTypeTest(),
                ObjectNames.PRIMITIVE_DATA_TYPE_TEST);
        /* DataTypeTest */
        registerMBean(new DataTypeTest(), ObjectNames.DATA_TYPE_TEST);
        /* BigDataTypeTest */
        registerMBean(new BigDataTypeTest(), ObjectNames.BIG_DATA_TYPE_TEST);
        /* BigDataTypeTest */
        registerMBean(new ObjectNameDataTypeTest(),
                ObjectNames.OBJECT_NAME_DATA_TYPE_TEST);
         /* TimeNotificationBroadcaster */
        registerMBean(new TimeNotificationBroadcaster(),
                ObjectNames.TIME_NOTIFICATION_BROADCASTER);
        /* Data Formatter */
        registerMBean(new DataFormat(),
                ObjectNames.DATA_FORMAT);
        /* OpenMBeanDataTypeTest */
        try {
            registerMBean(new OpenMBeanDataTypeTest(),
                    ObjectNames.OPEN_MBEAN_DATA_TYPE_TEST);
        } catch (OpenDataException e) {
            throw new RuntimeException(e);
        }

        if(!jmxmpConnector){
            /* start RMI connector */
            startJMXConnectorServer(port);
        }else{
            /* start the JMXMP Connector */
            startJMXMPConnectorServer(port);
        }
    }


    public static void registerMBean(Object instance, String objName) {
        try {
            ObjectName objectName = new ObjectName(objName);
            /* register with JSR160 MBean Server*/
            MBeanServer server = getMBeanServer();
            server.registerMBean(instance, objectName);
            /* register with JDK 1.5 MBean server */
            ManagementFactory.getPlatformMBeanServer().registerMBean(instance, objectName);
            logger.info("registered MBean: " + objName);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to register MBean: " + objName, e);
        }
    }

    /* start JSR160 RMI connector*/
    private static void startJMXConnectorServer(int port){
        try {
            /* attempt to start the rmi registry */
            startRMIRegistry(port);
            /* start the connector server */
            JMXServiceURL url = new JMXServiceURL(
              "service:jmx:rmi:///jndi/rmi://localhost:" + port + "/testApp");
            Map<String, Object> env = new HashMap<String, Object>();
            JMXAuthenticator authenticator = new MyJMXAuthenticator();
            env.put(JMXConnectorServer.AUTHENTICATOR, authenticator);
            JMXConnectorServer cs =
                 JMXConnectorServerFactory.newJMXConnectorServer(url,
                 env, getMBeanServer());
            cs.start();
            logger.info("JMXConnectorServer started. URL: " + url.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failure while starting RMI connector", e);
        }
    }

    private static class MyJMXAuthenticator implements JMXAuthenticator{
        public Subject authenticate(Object credentials) {
            System.out.println(credentials);
            return null;
        }
    }

    /* start JSR160 JMXMP connector*/
    private static void startJMXMPConnectorServer(int port){
        try {
            /* start the connector server */
            JMXServiceURL url = new JMXServiceURL(
              "service:jmx:jmxmp://localhost:" + port);
            Map<String, Object> env = new HashMap<String, Object>();
            JMXAuthenticator authenticator = new MyJMXAuthenticator();
            env.put(JMXConnectorServer.AUTHENTICATOR, authenticator);
            JMXConnectorServer cs =
                 JMXConnectorServerFactory.newJMXConnectorServer(url,
                 env, getMBeanServer());
            cs.start();
            logger.info("JMXConnectorServer started. URL: " + url.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failure while starting RMI connector", e);
        }
    }

    /* start rmi registry */
    private static void startRMIRegistry(int port){
        try {
            LocateRegistry.createRegistry(port);
            logger.info("rmiregistry started on: " + port);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error starting rmiregistry on " + port, e);
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    // Helper Methods

    /**
     * look up or create a MBeanServer
     * @return instance of MBeanServer
     */
    private static MBeanServer _getMBeanServer() {

        // synchronize to make sure that we don't create multiple MBeanServers
        synchronized (mbeanServerLock) {
            // if we got the instance in the mean time, just return it
            if (mbeanServer != null)
                return mbeanServer;
            return MBeanServerFactory.createMBeanServer();
        }
    }
}
