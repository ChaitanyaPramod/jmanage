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
package org.jmanage.core.modules.jboss;

import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ObjectInfo;
import org.jmanage.core.management.ObjectNotificationListener;
import org.jmanage.core.management.ObjectNotificationFilter;
import org.jmanage.core.modules.JMXServerConnection;
import org.jmanage.core.util.Loggers;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.jmx.adaptor.rmi.RMINotificationListener;

import javax.management.*;
import java.util.Set;
import java.util.List;
import java.util.logging.Logger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * date:  Oct 30, 2004
 * @author	Prem
 * @author Shashank Bellary
 * @author Rakesh Kalra
 */
public class JBossServerConnection extends JMXServerConnection {

    private static final Logger logger =
            Loggers.getLogger(JBossServerConnection.class);

    private final RMIAdaptor rmiAdaptor;

    public JBossServerConnection(RMIAdaptor rmiAdaptor) {
        super(rmiAdaptor, RMIAdaptor.class);
        assert rmiAdaptor != null;
        this.rmiAdaptor = rmiAdaptor;
    }

    public ObjectInfo getObjectInfo(ObjectName objectName) {

        String existingProtocolHandler =
                System.getProperty("java.protocol.handler.pkgs");
        logger.fine("Existing value for java.protocol.handler.pkgs: " +
                existingProtocolHandler);
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            // fix for Bug# 1211202
            System.setProperty("java.protocol.handler.pkgs",
                    "org.jmanage.net.protocol");
            MBeanInfo mbeanInfo = rmiAdaptor.getMBeanInfo(jmxObjName);
            return toObjectInfo(objectName, mbeanInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // todo: there is a minor bug here. if the existing value was null, it won't be reset
            if (existingProtocolHandler != null) {
                System.setProperty("java.protocol.handler.pkgs",
                        existingProtocolHandler);
            }
        }
    }

    public void addNotificationListener(ObjectName objectName,
                                        ObjectNotificationListener listener,
                                        ObjectNotificationFilter filter,
                                        Object handback) {

        try {
            MyRMINotificationListener notifListener =
                    toRMINotificationListener(listener);
            notifListener.export();
            notifications.put(listener, notifListener);
            NotificationFilter notifFilter =
                    toJMXNotificationFilter(filter);
            rmiAdaptor.addNotificationListener(toJMXObjectName(objectName),
                    notifListener, notifFilter, new String());// todo: handback is not used
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeNotificationListener(ObjectName objectName,
                                           ObjectNotificationListener listener,
                                           ObjectNotificationFilter filter,
                                           Object handback) {

        MyRMINotificationListener notifListener =
                (MyRMINotificationListener) notifications.remove(listener);
        assert notifListener != null;
        try {
            rmiAdaptor.removeNotificationListener(toJMXObjectName(objectName),
                    notifListener);
            notifListener.unexport();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static MyRMINotificationListener toRMINotificationListener(
            final ObjectNotificationListener listener) {

        return new MyRMINotificationListener(listener);
    }

    private static class MyRMINotificationListener
            implements RMINotificationListener{

        private final ObjectNotificationListener listener;

        MyRMINotificationListener(ObjectNotificationListener listener) {
            this.listener = listener;
        }

        public void export() throws RemoteException {
            UnicastRemoteObject.exportObject(this);
        }

        public void unexport() throws RemoteException {
            UnicastRemoteObject.unexportObject(this, true);
        }

        public void handleNotification(Notification notification, Object handback)
                throws RemoteException {
            listener.handleNotification(toObjectNotification(notification),
                    handback);
        }
    }
}


