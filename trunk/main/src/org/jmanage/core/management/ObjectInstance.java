package org.jmanage.core.management;

import java.io.Serializable;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class ObjectInstance implements Serializable {

    private ObjectName name;
    private String className;

    public ObjectInstance(String objName, String className) {
        this.name = new ObjectName(objName);
        this.className = className;
    }

    public ObjectInstance(ObjectName objectName, String className) {
        this.name = objectName;
        this.className = className;
    }

    public boolean equals(Object o) {
        if(o instanceof ObjectInstance){
            ObjectInstance oi = (ObjectInstance)o;
            if(oi.name.equals(this.name)
                    && oi.className.equals(this.className)){
                return true;
            }
        }
        return false;
    }

    public String getClassName() {
        return className;
    }

    public ObjectName getObjectName() {
        return name;
    }
}

