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
package org.jmanage.testapp.jsr160;

import javax.management.NotificationListener;
import javax.management.Notification;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

/**
 *
 * date:  Feb 22, 2005
 * @author	Rakesh Kalra
 */
public class NotificationListenerTest {

    public static void main(String[] args)
        throws Exception {

        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/testApp");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        mbsc.addNotificationListener(
                new ObjectName("jmanage:name=TimeNotificationBroadcaster"),
                new MyNotificationListener(), null, null);

        while(true){
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
            }
        }
    }

    public static class MyNotificationListener implements NotificationListener{

        public void handleNotification(Notification notification,
                                       Object bindVariables) {
            System.out.println("Notification: type=" + notification.getType()
                    + ", seq# " + notification.getSequenceNumber());
        }
    }
}
