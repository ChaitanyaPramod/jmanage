package org.jmanage.core.util;

/**
 *
 * date:  Jun 22, 2004
 * @author	Rakesh Kalra
 */
public class CoreUtils {

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
        return getRootDir() + "/logs";
    }
}