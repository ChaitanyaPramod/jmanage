package org.jmanage.test.mbeans;

/**
 *
 * date:  Dec 19, 2004
 * @author	Rakesh Kalra
 */
public interface ConfigurationMBean {

    /* read only properties */
    public String getAppName();
    public long getAppUptime();

    /* read write property */
    public int getMaxConnectionPoolSize();
    public void setMaxConnectionPoolSize(int size);


    /* sinple operation */
    /**@return previous uptime */
    public long resetAppUptime();
}
