package org.jmanage.core.management;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class ObjectName {

    private final String objectName;

    public ObjectName(String objectName){
        this.objectName = objectName;
    }

    public String getCanonicalName(){
        return objectName;
    }

    public String toString(){
        return objectName;
    }

    public boolean equals(Object obj){
        if(obj instanceof ObjectName){
            ObjectName on = (ObjectName)obj;
            return on.objectName.equals(this.objectName);
        }
        return false;
    }
}
