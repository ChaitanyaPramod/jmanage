package org.jmanage.core.config;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * TODO: implement toString method
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public abstract class ApplicationConfig {

    public static final String TYPE_WEBLOGIC = "Weblogic";

    private static final List EMPTY_LIST = new ArrayList();

    protected String appId;
    protected String name;
    protected String host;
    protected int port;
    protected String username;
    protected String password;
    protected Map paramValues;
    private List mbeanList;

    public ApplicationConfig(String applicationId,
                             String name,
                             String host,
                             int port){
        this.appId = applicationId;
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public ApplicationConfig(String applicationId,
                             String name,
                             String host,
                             int port,
                             String username,
                             String password,
                             Map paramValues){
        this(applicationId, name, host, port);
        this.username = username;
        this.password = password;
        this.paramValues = paramValues;
    }

    public String getApplicationId(){
        return appId;
    }

    /**
     * @return  the name of the application
     */
    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * If there are no additional parameters, the returned list is empty
     *
     * @return  list of additional configuration parameters(ConfigParam objects)
     */
    public List getAdditionalParameters(){
        return EMPTY_LIST;
    }

    public Map getParamValues(){
        return paramValues;
    }

    /**
     * @return list of MBeanConfig objects
     */
    public List getMBeans(){
        return mbeanList;
    }

    /**
     *
     * @param mbeanList list of MBeanConfig objects
     */
    public void setMBeans(List mbeanList){
        this.mbeanList = mbeanList;
    }

    /**
     * @return  type of application
     */
    public abstract String getType();

    /**
     * @return  the URL which will be used to connect to the application
     */
    public abstract String getURL();
}
