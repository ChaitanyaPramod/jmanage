package org.jmanage.core.util;

import java.util.logging.Logger;

/**
 * A helper class that is used for getting the logger for making log calls.
 *
 * date:  Aug 17, 2004
 * @author	Rakesh Kalra
 */
public class Loggers {

    /**
     * Returns a logger based on the package name. Every package has a shared
     * logger.
     * <p>
     * The logger in jmanage code, must be retrieved by calling this method.
     *
     * @param clazz     the class that wants to use the logger
     * @return  Logger instance for the package containing the class
     */
    public static Logger getLogger(Class clazz){
        return Logger.getLogger(clazz.getPackage().getName());
    }
}
