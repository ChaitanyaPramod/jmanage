package org.jmanage.core.config;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public interface ApplicationConfig {

    public static final String TYPE_WEBLOGIC = "Weblogic";

    /**
     * @return  the name of the application
     */
    public String getName();

    /**
     * @return  the type of application, e.g.: weblogic
     */
    public String getType();

    public String getHost();

    public int getPort();

    public String getURL();

    public String getUserName();

    public String getPassword();
}
