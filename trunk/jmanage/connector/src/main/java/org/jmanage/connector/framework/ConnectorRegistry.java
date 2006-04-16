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
package org.jmanage.connector.framework;

import org.apache.commons.modeler.Registry;
import org.apache.commons.modeler.ManagedBean;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ClassLoaderRepository;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.Loggers;

import javax.management.MBeanServerFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBean;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.io.InputStream;
import java.io.File;
import java.lang.reflect.Method;

/**
 * MBeanServer repository for each connector application. 
 * 
 * @author	Tak-Sang Chan
 */
public class ConnectorRegistry extends Registry {

    private static final Logger logger = Loggers.getLogger(ConnectorRegistry.class);    
    private static final String DOMAIN_CONNECTOR = "connector";
    private static final String MBEANS_DESCRIPTOR = "META-INF/mbeans-descriptors.xml";
    private static final String CLASS_CONNECTOR_SUPPORT = ConnectorSupport.class.getName();

    private static Map<String, ConnectorRegistry> entries = new HashMap<String, ConnectorRegistry>();

    private MBeanServer server;
    
    /**
     * Load mbeans of configured connectors into the registry.  The method
     * is call during startup.
     * 
     * @throws Exception
     */
    public static void load() throws Exception {
        List<ApplicationConfig> applications = ApplicationConfigManager.getApplications();
        for(ApplicationConfig config : applications) {
            String appType = config.getType();
            if (appType.equals("connector")) {
                create(config);
            }
        }
    }

    public synchronized static ConnectorRegistry getInstance(
            ApplicationConfig config) throws Exception {
        String appId = config.getApplicationId();
        ConnectorRegistry instance = (ConnectorRegistry) entries.get(appId);
        if (instance == null) {
            instance = create(config);
        }

        return instance;
    }
    
    public static void remove(ApplicationConfig config) throws Exception {
        String appId = config.getApplicationId();
        ConnectorRegistry registry = entries.remove(appId);
        if (registry != null) {
            registry.unregisterAllMBeans(appId);
        }
    }
    
    private static ConnectorRegistry create(ApplicationConfig config) throws Exception {

        Map paramValues = config.getParamValues();
        String appId = config.getApplicationId();
        String connectorId = (String) paramValues.get("connectorId");

        File file1 = new File(CoreUtils.getConnectorDir() + File.separatorChar + connectorId);

        URL[] urls = new URL[]{file1.toURL()};
        ClassLoader cl = ClassLoaderRepository.getClassLoader(urls, true);

        //URLClassLoader cl = new URLClassLoader(urls,
        //        ConnectorMBeanRegistry.class.getClassLoader());

        Registry.MODELER_MANIFEST = MBEANS_DESCRIPTOR;
        URL res = cl.getResource(MBEANS_DESCRIPTOR);

        logger.log(Level.INFO, "Application ID   : " + appId);        
        logger.log(Level.INFO, "Connector Archive: " + file1.getAbsoluteFile());                        
        logger.log(Level.INFO, "MBean Descriptor : " + res.toString());

        //Thread.currentThread().setContextClassLoader(cl);
        //Registry.setUseContextClassLoader(true);

        ConnectorRegistry registry = new ConnectorRegistry();
        registry.loadMetadata(cl);

        String[] mbeans = registry.findManagedBeans();

        MBeanServer server = MBeanServerFactory.newMBeanServer(DOMAIN_CONNECTOR);
        
        String objName = ":appId=" + appId + ",appType=connector,appName=" + config.getName()
                + ",connectorType=" + connectorId;

        for (int i = 0; i < mbeans.length; i++) {
            ManagedBean managed = registry.findManagedBean(mbeans[i]);
            String clsName = managed.getType();
            String domain = managed.getDomain();
            if (domain == null) {
                domain = DOMAIN_CONNECTOR;
            }

            Class cls = Class.forName(clsName, true, cl);
            Object obj = cls.newInstance();

            // Call the initialize method if the MBean extends ConnectorSupport class
            if (cls.getSuperclass().getName().equals(CLASS_CONNECTOR_SUPPORT)) {
                Method method = cls.getMethod("initialize", new Class[] {Map.class});
                Map props = config.getParamValues();
                method.invoke(obj, new Object[] {props});
            }
            ModelMBean mm = managed.createMBean(obj);

            String beanObjName = domain + objName + ",name=" + mbeans[i];
            server.registerMBean(mm, new ObjectName(beanObjName));
        }

        registry.setMBeanServer(server);
        entries.put(appId, registry);
        return registry;
    }
    
    public void setMBeanServer(MBeanServer server) {
        this.server = server;
    }

    public MBeanServer getMBeanServer() {
        return this.server;
    }
    
    @SuppressWarnings("unchecked")
    private void unregisterAllMBeans(String appId) throws Exception {
        Set<ObjectName> objNames = this.server.queryNames(
                new ObjectName("*:appId=" + appId + ",*"), null);
        for(ObjectName name : objNames) {
            logger.info("Unregistered MBean: " + name);
            this.server.unregisterMBean(name);
        }
    }
    
    @SuppressWarnings("deprecation")
    private void loadMetadata(ClassLoader cl) throws Exception {
        // Fix a bug in the Registry class
        try {
            Enumeration en = cl.getResources(MODELER_MANIFEST);
            while (en.hasMoreElements()) {
                URL url = (URL) en.nextElement();
                InputStream is = url.openStream();
                loadDescriptors("MbeansDescriptorsDOMSource", is, null);
            }
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }
    }
}
