package org.jmanage.core.modules;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ModuleConfig {

    private String id;
    private String name;
    private String configClass;
    private String connectionFactory;

    public ModuleConfig(String id,
                        String name,
                        String configClass,
                        String connectionFactory){
        this.id = id;
        this.name = name;
        this.configClass = configClass;
        this.connectionFactory = connectionFactory;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConfigClass() {
        return configClass;
    }

    public String getConnectionFactory() {
        return connectionFactory;
    }
}
