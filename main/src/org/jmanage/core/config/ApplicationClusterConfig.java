package org.jmanage.core.config;

import java.util.List;
import java.util.LinkedList;

/**
 *
 * date:  Sep 15, 2004
 * @author	Rakesh Kalra
 */
public class ApplicationClusterConfig extends ApplicationConfig {

    private List applications = new LinkedList();

    public ApplicationClusterConfig(String id, String name){
        setApplicationId(id);
        setName(name);
    }

    /**
     * @return true
     */
    public boolean isCluster(){
        return true;
    }

    /**
     * This method returns a list of applications in this cluster.
     *
     * @return  list of applications in this cluster
     */
    public List getApplications(){
        return applications;
    }

    public void setApplications(List appConfigList) {
        this.applications = appConfigList;
    }

    /**
     * Adds a application to this cluster.
     *
     * @param config    ApplicationConfig to be added to this cluster
     */
    public void addApplication(ApplicationConfig config){
        applications.add(config);
    }

    public void addAllApplications(List appConfigList){
        applications.addAll(appConfigList);
    }

    /**
     * Removes the application from the cluster.
     *
     * @param config
     * @return true: if the application was removed from the cluster
     */
    public boolean removeApplication(ApplicationConfig config){
        return applications.remove(config);
    }

}
