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

    public static String getRootDir(){
        return System.getProperty(SystemProperties.JMANAGE_ROOT);
    }

    public static String getConfigDir(){
        return getRootDir() + "/config";
    }

    public static String getWebDir(){
        return getRootDir() + "/web";
    }

    public static String getModuleDir(String moduleId){
        return getRootDir() + "/modules/" + moduleId;
    }

    public static String getLogDir(){
        return getRootDir() + "/log";
    }
}