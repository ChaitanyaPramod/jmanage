package org.jmanage.core.management;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ObjectParameterInfo extends ObjectFeatureInfo {

    private String type;

    public ObjectParameterInfo(String name, String description, String type) {
        super(name, description);
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
