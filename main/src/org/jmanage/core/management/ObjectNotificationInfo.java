package org.jmanage.core.management;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ObjectNotificationInfo extends ObjectFeatureInfo {

    private String[] types;

    public ObjectNotificationInfo(String[] types, String name, String description) {
        super(name, description);
        this.types = types;
    }

    public String[] getNotifTypes() {
        return types;
    }
}
