package org.jmanage.core.config;

import org.jmanage.core.crypto.Crypto;

import java.util.*;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public abstract class ApplicationConfig {

    private static final List EMPTY_LIST = new ArrayList();

    private String appId;
    private String type;
    private String name;
    private String host;
    private Integer port;
    private String url;
    private String username;
    private String password;
    protected Map paramValues;
    private List mbeanList = new LinkedList();

    public String getApplicationId(){
        return appId;
    }

    public void setApplicationId(String appId){
        assert this.appId == null;
        this.appId = appId;
    }

    /**
     * @return  type of application
     */
    public String getType(){
        return type;
    }

    public void setType(String type){
        assert this.type == null;
        this.type = type;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
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
     * @return true: if its a application cluster; false: otherwise
     */
    public boolean isCluster(){
        return false;
    }

    /**
     * If this is a application cluster, this method returns a list of
     * applications in this cluster.
     *
     * @return  list of applications in this cluster
     */
    public List getApplications(){
        return null;
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
