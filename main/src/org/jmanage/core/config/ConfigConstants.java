package org.jmanage.core.config;

import org.jmanage.core.util.CoreUtils;

/**
 *
 * Date: Jun 19, 2004
 * @author  Shashank, Rakesh Kalra
 */
public interface ConfigConstants {

    /*  Default config file to use  */
    public String DEFAULT_CONFIG_FILE_NAME = CoreUtils.getConfigDir()
            + "/config.xml";
    public String JMANAGE_PROPERTY_FILE = CoreUtils.getConfigDir() +
            "/jmanage.properties";
    public String APPLICATION_CONFIG = "application-config";
    public String APPLICATIONS = "applications";
    public String APPLICATION_CLUSTER = "application-cluster";
    public String APPLICATION = "application";
    public String PARAMETER = "param";
    public String PARAMETER_NAME = "param-name";
    public String PARAMETER_VALUE = "param-value";
    public String APPLICATION_ID = "id";
    public String APPLICATION_NAME = "name";
    public String APPLICATION_TYPE = "type";
    public String HOST = "host";
    public String PORT = "port";
    public String URL = "url";
    public String USERNAME = "username";
    public String PASSWORD = "password";

    public String MBEANS = "mbeans";
    public String MBEAN = "mbean";
    public String MBEAN_NAME = "name";
    public String MBEAN_OBJECT_NAME = "object-name";
}
