package org.jmanage.core.management;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ObjectInfo {

    private String description;
    private boolean isOpen;
    private String className;
    private ObjectAttributeInfo[] attributes;
    private ObjectOperationInfo[] operations;
    private ObjectConstructorInfo[] constructors;
    private ObjectNotificationInfo[] notifications;

    public ObjectInfo(String className,
                      String description,
                      ObjectAttributeInfo[] attributes,
                      ObjectConstructorInfo[] constructors,
                      ObjectOperationInfo[] operations,
                      ObjectNotificationInfo[] notifications) {
        this.className = className;
        this.description = description;
        this.attributes = attributes;
        this.constructors = constructors;
        this.operations = operations;
        this.notifications = notifications;
    }

    public ObjectAttributeInfo[] getAttributes() {
        return attributes;
    }

    public String getClassName() {
        return className;
    }

    public ObjectConstructorInfo[] getConstructors() {
        return constructors;
    }

    public String getDescription() {
        return description;
    }

    public ObjectNotificationInfo[] getNotifications() {
        return notifications;
    }

    public ObjectOperationInfo[] getOperations() {
        return operations;
    }
}
