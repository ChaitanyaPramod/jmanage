package org.jmanage.core.management;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ObjectAttributeInfo extends ObjectFeatureInfo {

    private String attributeType;
    private boolean isWrite;
    private boolean isRead;
    private boolean isIs;

    public ObjectAttributeInfo(String name,
                               String description,
                               String attributeType,
                               boolean isWrite,
                               boolean isRead,
                               boolean isIs) {
        super(name, description);
        this.attributeType = attributeType;
        this.isWrite = isWrite;
        this.isRead = isRead;
        this.isIs = isIs;
    }

    public String getType() {
        return attributeType;
    }

    public boolean isIs() {
        return isIs;
    }

    public boolean isReadable() {
        return isRead;
    }

    public boolean isWritable() {
        return isWrite;
    }
}