package org.jmanage.core.util;

/**
 *
 * date:  Jun 22, 2004
 * @author	Rakesh Kalra
 */
public class CoreUtils {

    public static Object getTypedValue(String value, String type){
        if(type.equals("java.lang.String")){
            return value;
        }else if(type.equals("int")){
            return new Integer(value);
        }else if(type.equals("long")){
            return new Long(value);
        }else if(type.equals("boolean")){
            return Boolean.valueOf(value);
        }
        throw new RuntimeException("Unknown type: " + type);
    }
}
