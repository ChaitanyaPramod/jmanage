package org.jmanage.core.config;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public abstract class AbstractApplicationConfig implements ApplicationConfig {

    protected String name;
    protected String host;
    protected int port;
    protected String username;
    protected String password;

    public AbstractApplicationConfig(String name, String host, int port){
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public AbstractApplicationConfig(String name, String host, int port,
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
}
