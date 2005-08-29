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

import java.util.Map;
import java.util.HashMap;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class ModuleRegistry {

    private static Map modules = new HashMap();

    // TODO: modules should be read from jmanage-config, or dynamically loaded
    //      from modules folder.


    static {

        MetaApplicationConfig metaConfig = null;

        /* weblogic module */
        metaConfig = new MetaApplicationConfig(true, true, false, true, true,
                "org.jmanage.core.modules.weblogic.WeblogicApplicationConfig");
        modules.put(ApplicationType.WEBLOGIC,
                new ModuleConfig(ApplicationType.WEBLOGIC,
                        "Weblogic 6.1",
                        metaConfig,
                        "org.jmanage.core.modules.weblogic.WLServerConnectionFactory"));

        /* tomcat module */
        /** TODO: this needs to fixed - rk
        metaConfig = new MetaApplicationConfig(true, true, false, false, false,
                "org.jmanage.core.modules.tomcat.TomcatApplicationConfig");
        modules.put(ApplicationType.TOMCAT,
                new ModuleConfig(ApplicationType.TOMCAT,
                        "Tomcat",
                        metaConfig,
                        "org.jmanage.core.modules.tomcat.TomcatServerConnectionFactory"));
        */
        /* jsr160 module */
        metaConfig = new MetaApplicationConfig(false, false, true, true, true,
                "org.jmanage.core.modules.jsr160.JSR160ApplicationConfig");
        modules.put(ApplicationType.JSR160,
                new ModuleConfig(ApplicationType.JSR160,
                        "JSR160",
                        metaConfig,
                        "org.jmanage.core.modules.jsr160.JSR160ServerConnectionFactory"));

        /* jboss module */
        metaConfig = new MetaApplicationConfig(true, true, false, true, true,
                "org.jmanage.core.modules.jboss.JBossApplicationConfig");
        modules.put(ApplicationType.JBOSS,
                new ModuleConfig(ApplicationType.JBOSS,
                        "JBoss 3.2.4",
                        metaConfig,
                        "org.jmanage.core.modules.jboss.JBossServerConnectionFactory"));
        /*  WebSphere module    */
        metaConfig = new MetaApplicationConfig(true, true, false, true, true,
                "org.jmanage.core.modules.websphere.WebSphereApplicationConfig");
        modules.put(ApplicationType.WEBSPHERE,
                new ModuleConfig(ApplicationType.WEBSPHERE,
                        "WebSphere",
                        metaConfig,
                        "org.jmanage.core.modules.websphere.WebSphereServerConnectionFactory"));
        /*  SNMP module    */
        metaConfig = new MetaApplicationConfig(true, true, false, false, false,
                "org.jmanage.core.modules.snmp.SNMPApplicationConfig");
        modules.put(ApplicationType.SNMP,
                new ModuleConfig(ApplicationType.SNMP,
                        "SNMP",
                        metaConfig,
                        "org.jmanage.core.modules.snmp.SNMPAgentConnectionFactory"));
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
