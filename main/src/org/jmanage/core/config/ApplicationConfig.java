package org.jmanage.core.config;

import org.jmanage.core.crypto.Crypto;

import java.util.*;

/**
 * TODO: Is "Server" a better term than "Application" -rk ?
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public abstract class ApplicationConfig {

    public static final String TYPE_WEBLOGIC = "weblogic";

    private static final List EMPTY_LIST = new ArrayList();

    protected String appId;
    private String name;
    private String host;
    private int port;
    private String username;
    private String password;
    protected Map paramValues;
    private List mbeanList;

    public ApplicationConfig(String applicationId,
                             String name,
                             String host,
                             int port){
        this.appId = applicationId;
        this.setName(name);
        this.setHost(host);
        this.setPort(port);
        this.mbeanList = new LinkedList();
    }

    public ApplicationConfig(String applicationId,
                             String name,
                             String host,
                             int port,
                             String username,
                             String password,
                             Map paramValues){
        this(applicationId, name, host, port);
        this.setUsername(username);
        this.setPassword(password);
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

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setParamValues(Map paramValues){
        this.paramValues = paramValues;
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
        if(mbeanList != null){
            this.mbeanList = mbeanList;
        }else{
            this.mbeanList = new LinkedList();
        }
    }

    public boolean equals(Object obj){
        if(obj instanceof ApplicationConfig){
            ApplicationConfig config = (ApplicationConfig)obj;
            return config.getApplicationId().equals(this.getApplicationId());
        }
        return false;
    }

    public String toString(){
        return getApplicationId() + ";" + getName();
    }

    /**
     * @return  type of application
     */
    public abstract String getType();

    /**
     * @return  the URL which will be used to connect to the application
     */
    public abstract String getURL();

    public boolean containsMBean(String objectName) {

        for(Iterator it=mbeanList.iterator(); it.hasNext();){
            MBeanConfig mbeanConfig = (MBeanConfig)it.next();
            if(mbeanConfig.getObjectName().equals(objectName)){
                return true;
            }
        }
        return false;
    }

    public MBeanConfig removeMBean(String objectName){

        for(Iterator it=mbeanList.iterator(); it.hasNext();){
            MBeanConfig mbeanConfig = (MBeanConfig)it.next();
            if(mbeanConfig.getObjectName().equals(objectName)){
                mbeanList.remove(mbeanConfig);
                return mbeanConfig;
            }
        }
        return null;
    }

    public void addMBean(MBeanConfig mbeanConfig){

        assert !containsMBean(mbeanConfig.getObjectName());
        mbeanList.add(mbeanConfig);
    }
}
