package org.jmanage.core.config;

/**
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra
 */
public class MBeanConfig {

    private String name;
    private String objectName;

    public MBeanConfig(String name, String objectName){
        this.setName(name);
        this.setObjectName(objectName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
