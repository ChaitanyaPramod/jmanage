package org.jmanage.core.modules;

import org.jmanage.core.config.ApplicationType;
import org.jmanage.core.config.MetaApplicationConfig;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ModuleRegistry {

    private static Map modules = new HashMap();

    // TODO: modules should be read from jmanage-config, or dynamically loaded
    //      from modules folder.


    static {

        MetaApplicationConfig metaConfig = null;

        /* weblogic module */
        metaConfig = new MetaApplicationConfig(true, true, false, true, true,
                "org.jmanage.modules.weblogic.WeblogicApplicationConfig");
        modules.put(ApplicationType.WEBLOGIC,
                new ModuleConfig(ApplicationType.WEBLOGIC,
                        "Weblogic 6.1",
                        metaConfig,
                        "org.jmanage.modules.weblogic.WLServerConnectionFactory"));

        /* tomcat module */
        metaConfig = new MetaApplicationConfig(true, true, false, false, false,
                "org.jmanage.modules.tomcat.TomcatApplicationConfig");
        modules.put(ApplicationType.TOMCAT,
                new ModuleConfig(ApplicationType.TOMCAT,
                        "Tomcat",
                        metaConfig,
                        "org.jmanage.modules.tomcat.TomcatServerConnectionFactory"));

        /* jsr160 module */
        metaConfig = new MetaApplicationConfig(false, false, true, true, true,
                "org.jmanage.modules.jsr160.JSR160ApplicationConfig");
        modules.put(ApplicationType.JSR160,
                new ModuleConfig(ApplicationType.JSR160,
                        "JSR160",
                        metaConfig,
                        "org.jmanage.modules.jsr160.JSR160ServerConnectionFactory"));

        /* jboss module */
        metaConfig = new MetaApplicationConfig(true, true, false, false, false,
                "org.jmanage.modules.jboss.JBossApplicationConfig");
        modules.put(ApplicationType.JBOSS,
                new ModuleConfig(ApplicationType.JBOSS,
                        "JBoss 3.2.4",
                        metaConfig,
                        "org.jmanage.modules.jboss.JBossServerConnectionFactory"));
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