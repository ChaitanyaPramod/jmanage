package org.jmanage.core.management;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ObjectFeatureInfo {

    protected String name;
    protected String description;

    public ObjectFeatureInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
