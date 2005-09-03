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
package org.jmanage.core.config;

import org.jmanage.core.config.ApplicationType;
import org.jmanage.core.config.MetaApplicationConfig;
import org.jmanage.core.config.ModuleConfig;
import org.jmanage.core.util.Loggers;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class ModuleRegistry {

    private static final Logger logger = Loggers.getLogger(ModuleRegistry.class);

    private static Map modules = new HashMap();

    // TODO: modules should be read from jmanage-config, or dynamically loaded
    //      from modules folder.


    static {

        MetaApplicationConfig metaConfig = null;

        /* weblogic module */
        try {
            metaConfig = new MetaApplicationConfig(true, true, false, true, true,
                    "org.jmanage.core.modules.weblogic.WeblogicApplicationConfig");
            modules.put(ApplicationType.WEBLOGIC,
                    new ModuleConfig(ApplicationType.WEBLOGIC,
                            "Weblogic 6.1",
                            metaConfig,
                            "org.jmanage.core.modules.weblogic.WLServerConnectionFactory"));
        } catch (ModuleConfig.ModuleNotFoundException e) {
            logger.warning(ApplicationType.WEBLOGIC + " module not found");
        }

        /* jsr160 module */
        try {
            metaConfig = new MetaApplicationConfig(false, false, true, true, true,
                    "org.jmanage.core.modules.jsr160.JSR160ApplicationConfig");
            modules.put(ApplicationType.JSR160,
                    new ModuleConfig(ApplicationType.JSR160,
                            "JSR160",
                            metaConfig,
                            "org.jmanage.core.modules.jsr160.JSR160ServerConnectionFactory"));
        } catch (ModuleConfig.ModuleNotFoundException e) {
            logger.warning(ApplicationType.JSR160 + " module not found");
        }

        /* jboss module */
        try {
            metaConfig = new MetaApplicationConfig(true, true, false, true, true,
                    "org.jmanage.core.modules.jboss.JBossApplicationConfig");
            modules.put(ApplicationType.JBOSS,
                    new ModuleConfig(ApplicationType.JBOSS,
                            "JBoss 3.2.4",
                            metaConfig,
                            "org.jmanage.core.modules.jboss.JBossServerConnectionFactory"));
        } catch (ModuleConfig.ModuleNotFoundException e) {
            logger.warning(ApplicationType.JBOSS + " module not found");
        }

        /*  WebSphere module    */
        try {
            metaConfig = new MetaApplicationConfig(true, true, false, true, true,
                    "org.jmanage.core.modules.websphere.WebSphereApplicationConfig");
            modules.put(ApplicationType.WEBSPHERE,
                    new ModuleConfig(ApplicationType.WEBSPHERE,
                            "WebSphere",
                            metaConfig,
                            "org.jmanage.core.modules.websphere.WebSphereServerConnectionFactory"));
        } catch (ModuleConfig.ModuleNotFoundException e) {
            logger.warning(ApplicationType.WEBSPHERE + " module not found");
        }

        /*  SNMP module    */
        try {
            metaConfig = new MetaApplicationConfig(true, true, false, false, false,
                    "org.jmanage.core.modules.snmp.SNMPApplicationConfig");
            modules.put(ApplicationType.SNMP,
                    new ModuleConfig(ApplicationType.SNMP,
                            "SNMP",
                            metaConfig,
                            "org.jmanage.core.modules.snmp.SNMPAgentConnectionFactory"));
        } catch (ModuleConfig.ModuleNotFoundException e) {
            logger.warning(ApplicationType.SNMP + " module not found");
        }
    }

    public static ModuleConfig getModule(String type){
        final ModuleConfig config = (ModuleConfig)modules.get(type);
        assert config != null: "Invalid module id: " + type;
        return config;
    }

    public static Map getModules() {
        return modules;
    }
}
