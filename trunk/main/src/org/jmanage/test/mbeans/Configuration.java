package org.jmanage.test.mbeans;

/**
 *
 * date:  Dec 19, 2004
 * @author	Rakesh Kalra
 */
public class Configuration implements ConfigurationMBean {

    private final String appName = "testApp";
    private long whenStarted = System.currentTimeMillis();
    private int maxConnectionPoolSize = 10;

    /* read only properties */
    public String getAppName() {
        return appName;
    }

    public long getAppUptime() {
        return System.currentTimeMillis() - whenStarted;
    }

    /* read write property */
    public int getMaxConnectionPoolSize() {
        return maxConnectionPoolSize;
    }

    public void setMaxConnectionPoolSize(int size) {
        this.maxConnectionPoolSize = size;
    }

    /**@return previous uptime */
    public long resetAppUptime() {
        long prevUptime = getAppUptime();
        whenStarted = System.currentTimeMillis();
        return prevUptime;
    }
}
