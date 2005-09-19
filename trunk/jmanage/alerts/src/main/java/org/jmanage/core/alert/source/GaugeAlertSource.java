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

import org.jmanage.core.alert.AlertSource;
import org.jmanage.core.alert.AlertHandler;
import org.jmanage.core.alert.AlertInfo;
import org.jmanage.core.config.AlertSourceConfig;
import org.jmanage.core.management.*;
import org.jmanage.core.util.Loggers;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.io.IOException;

/**
 * Date: Aug 31, 2005 11:41:59 AM
 * @author Bhavana
 */
public class GaugeAlertSource extends AlertSource{

    private static final Logger logger =
            Loggers.getLogger(GaugeAlertSource.class);

    private AlertHandler handler;
    private ServerConnection connection;
    private ObjectName monitorObjName = null;
    private ObjectNotificationListener listener = null;
    private ObjectNotificationFilterSupport filter = null;

    public GaugeAlertSource(AlertSourceConfig sourceConfig){
        super(sourceConfig);
    }

    public void register(AlertHandler handler,
                         String alertId,
                         String alertName){

        assert this.handler == null;
        assert connection == null;

        this.handler = handler;

        /* start looking for this notification */
        connection = ServerConnector.getServerConnection(
                sourceConfig.getApplicationConfig());

        monitorObjName = new ObjectName("jmanage:name=" + alertName +
                ",id=" + alertId + ",type=GaugeMonitor");

        /* check if the MBean is already registered */
        Set mbeans = connection.queryNames(monitorObjName);
        if(mbeans != null && mbeans.size() > 0){
            /* remove the MBean */
            connection.unregisterMBean(monitorObjName);
        }

        /* create the MBean */
        connection.createMBean("javax.management.monitor.GaugeMonitor",
                monitorObjName, null, null);
        /* set attributes */
        List attributes = new LinkedList();
        attributes.add(new ObjectAttribute("GranularityPeriod", new  Long(5000)));
        attributes.add(new ObjectAttribute("NotifyHigh", Boolean.TRUE));
        attributes.add(new ObjectAttribute("NotifyLow", Boolean.TRUE));
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
        /* set thresholds */
        Object[] params = new Object[]{sourceConfig.getHighThreshold(),
                                       sourceConfig.getLowThreshold()};
        String[] signature = new String[]{Number.class.getName(),
                Number.class.getName()};
        connection.invoke(monitorObjName, "setThresholds", params, signature);
        /* start the monitor */
        connection.invoke(monitorObjName, "start", new Object[0], new String[0]);

        /* now look for notifications from this mbean */
        listener = new ObjectNotificationListener(){
            public void handleNotification(ObjectNotification notification,
                                           Object handback) {
                try {
                    GaugeAlertSource.this.handler.handle(
                            new AlertInfo(notification));
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error while handling alert", e);
                }
            }
        };
        filter = new ObjectNotificationFilterSupport();
        filter.enableType("jmx.monitor.gauge.high");
        filter.enableType("jmx.monitor.gauge.low");
        filter.enableType("jmx.monitor.error.attribute");
        filter.enableType("jmx.monitor.error.type");
        filter.enableType("jmx.monitor.error.mbean");
        filter.enableType("jmx.monitor.error.runtime");
        filter.enableType("jmx.monitor.error.threshold");
        connection.addNotificationListener(monitorObjName,
                listener, filter, null);
    }

    public void unregister() {
        assert connection != null;
        assert monitorObjName != null;

        try {
            /* remove notification listener */
            connection.removeNotificationListener(monitorObjName, listener,
                   filter, null);
        } catch (Exception e) {
            logger.log(Level.WARNING,
                    "Error while Removing Notification Listener", e);
        }

        try {
            /* unregister GaugeMonitor MBean */
            connection.unregisterMBean(monitorObjName);
        } catch (Exception e) {
            logger.log(Level.WARNING,
                    "Error while unregistering MBean: " + monitorObjName, e);
        }

        /* close the connection */
        try {
           connection.close();
        } catch (IOException e) {
           logger.log(Level.SEVERE, "Error while closing connection", e);
        }

        connection = null;
        handler = null;
        listener = null;

        filter = null;
    }
}
