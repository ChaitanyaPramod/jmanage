package org.jmanage.core.config;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public abstract class ApplicationConfig {

    public static final String TYPE_WEBLOGIC = "Weblogic";

    private static final List EMPTY_LIST = new ArrayList();

    protected String name;
    protected String host;
    protected int port;
    protected String username;
    protected String password;

    public ApplicationConfig(String name, String host, int port){
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public ApplicationConfig(String name, String host, int port,
                                     String username, String password){
        this(name, host, port);
        this.username = username;
        this.password = password;
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

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * If there are no additional parameters, the returned list is empty
     *
     * @return  list of additional configuration parameters
     */
    public List getAdditionalParameters(){
        return EMPTY_LIST;
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
