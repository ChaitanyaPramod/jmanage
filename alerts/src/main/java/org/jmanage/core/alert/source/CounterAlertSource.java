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
package org.jmanage.core.alert.source;

import org.jmanage.core.alert.AlertInfo;
import org.jmanage.core.config.AlertSourceConfig;
import org.jmanage.core.management.*;
import org.jmanage.core.util.Loggers;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;

/**
 * Date: Aug 31, 2005 11:41:59 AM
 * @author Bhavana
 */
public class CounterAlertSource extends MBeanAlertSource{

    private static final Logger logger =
            Loggers.getLogger(CounterAlertSource.class);

    private ObjectName monitorObjName = null;
    private ObjectNotificationListener listener = null;
    private ObjectNotificationFilterSupport filter = null;

    public CounterAlertSource(AlertSourceConfig sourceConfig){
        super(sourceConfig);
    }

    protected void registerInternal(){

        /* start looking for this notification */
        monitorObjName = new ObjectName("jmanage.alerts:name=" + alertName +
                ",id=" + alertId + ",type=CounterMonitor");

        /* check if the MBean is already registered */
        Set mbeans = connection.queryNames(monitorObjName);
        if(mbeans != null && mbeans.size() > 0){
            /* remove the MBean */
            connection.unregisterMBean(monitorObjName);
        }

        /* create the MBean */
        connection.createMBean("javax.management.monitor.CounterMonitor",
                monitorObjName, new Object[0], new String[0]);
        /* set attributes */
        List<ObjectAttribute> attributes = new LinkedList<ObjectAttribute>();
        attributes.add(new ObjectAttribute("GranularityPeriod", new  Long(100)));
        attributes.add(new ObjectAttribute("Notify", Boolean.TRUE));
        attributes.add(new ObjectAttribute("InitThreshold", new Integer(0)));
        attributes.add(new ObjectAttribute("Offset", new Integer(1)));
        attributes.add(new ObjectAttribute("ObservedAttribute",
                sourceConfig.getAttributeName()));
        // note the following is deprecated, but this is what weblogic exposes
        attributes.add(new ObjectAttribute("ObservedObject",
                connection.buildObjectName(sourceConfig.getObjectName())));
        connection.setAttributes(monitorObjName, attributes);
        /* add observed object */
        /*
        connection.invoke(monitorObjName, "addObservedObject",
                new Object[]{new ObjectName(sourceConfig.getObjectName())},
                new String[]{"javax.management.ObjectName"});
        */
        
        /* start the monitor */
        connection.invoke(monitorObjName, "start", new Object[0], new String[0]);

        /* now look for notifications from this mbean */
        listener = new ObjectNotificationListener(){
            public void handleNotification(ObjectNotification notification,
                                           Object handback) {
                try {
                    CounterAlertSource.this.handler.handle(
                            new AlertInfo(notification));
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error while handling alert", e);
                }
            }
        };
        filter = new ObjectNotificationFilterSupport();
        filter.enableType("jmx.monitor.counter");      
        filter.enableType("jmx.monitor.error"); 
        
        connection.addNotificationListener(monitorObjName,
                listener, filter, null);
    }

    protected void unregisterInternal() {
        assert connection != null;
        assert monitorObjName != null;

        try {
            /* remove notification listener */
            connection.removeNotificationListener(monitorObjName, listener,
                   filter, null);
        } catch (Exception e) {
            logger.log(Level.WARNING,
                    "Error while Removing Notification Listener. error: " +
                    e.getMessage());
        }

        try {
            /* unregister GaugeMonitor MBean */
            connection.unregisterMBean(monitorObjName);
        } catch (Exception e) {
            logger.log(Level.WARNING,
                    "Error while unregistering MBean: " + monitorObjName +
                    ". error: " + e.getMessage());
        }

        listener = null;
        filter = null;
    }
}