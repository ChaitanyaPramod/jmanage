package org.jmanage.core.management;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ObjectAttribute {

    private String name;
    private Object value;

    public ObjectAttribute(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public boolean equals(Object o) {
        if(o instanceof ObjectAttribute){
            ObjectAttribute attr = (ObjectAttribute)o;
            if(attr.name.equals(this.name) && attr.value.equals(this.value)){
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
