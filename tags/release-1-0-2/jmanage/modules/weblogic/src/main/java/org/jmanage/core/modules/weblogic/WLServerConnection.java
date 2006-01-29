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
package org.jmanage.core.modules.weblogic;

import org.jmanage.core.management.*;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.modules.JMXServerConnection;

import javax.management.*;
import java.util.*;

import weblogic.management.RemoteNotificationListener;
import weblogic.management.RemoteMBeanServer;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class WLServerConnection extends JMXServerConnection{

    private final RemoteMBeanServer mbeanServer;

    public WLServerConnection(MBeanServer mbeanServer){
        super(mbeanServer, MBeanServer.class);
        assert mbeanServer != null;
        this.mbeanServer = (RemoteMBeanServer)mbeanServer;
    }

    public void addNotificationListener(ObjectName objectName,
                                        ObjectNotificationListener listener,
                                        ObjectNotificationFilter filter,
                                        Object handback){

        NotificationListener notifListener =
                toRemoteNotificationListener(listener);
        notifications.put(listener, notifListener);
        NotificationFilter notifFilter =
                toJMXNotificationFilter(filter);
        try {
            mbeanServer.addNotificationListener(toJMXObjectName(objectName),
                    notifListener, notifFilter, handback);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static NotificationListener toRemoteNotificationListener(
            final ObjectNotificationListener listener){

        return new RemoteNotificationListener(){
            public void handleNotification(Notification notification,
                                       Object handback) {
                listener.handleNotification(toObjectNotification(notification),
                        handback);
            }
        };
    }

    public void removeNotificationListener(ObjectName objectName,
                                           ObjectNotificationListener listener,
                                           ObjectNotificationFilter filter,
                                           Object handback){

        NotificationListener notifListener =
                (NotificationListener)notifications.remove(listener);
        assert notifListener != null;

        try {
            mbeanServer.removeNotificationListener(toJMXObjectName(objectName),
                    notifListener);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ListenerNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}