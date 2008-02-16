/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.testapp.mbeans;

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

    public void throwError(){
        throw new RuntimeException("This is a test error.");
    }
}
