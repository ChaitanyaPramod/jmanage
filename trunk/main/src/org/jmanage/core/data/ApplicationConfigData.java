package org.jmanage.core.data;

/**
 *
 * date:  Jan 9, 2005
 * @author	Rakesh Kalra
 */
public class ApplicationConfigData {

    private String appId;
    private String name;
    private String host;
    private Integer port;
    private String url;
    private String username;
    private String password;
    private String type;

    public String getApplicationId() {
        return appId;
    }

    public void setApplicationId(String appId) {
        this.appId = appId;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
