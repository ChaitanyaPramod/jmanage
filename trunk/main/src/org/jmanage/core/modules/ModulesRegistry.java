package org.jmanage.core.modules;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ModulesRegistry {

    private static Map modules = new HashMap();

    // TODO: modules should be read from jmanage-config, or dynamically loaded
    //      from modules folder.

    static {
        modules.put("weblogic",
                new ModuleConfig("weblogic",
                        "Weblogic 6.1",
                        "org.jmanage.modules.weblogic.WeblogicApplicationConfig",
                        "org.jmanage.modules.weblogic.WLServerConnectionFactory"));
    }

    public static ModuleConfig getModule(String id){
        return (ModuleConfig)modules.get(id);
    }
}
