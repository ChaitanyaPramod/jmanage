/**
* Copyright (c) 2004-2005 jManage.org
*
* This is a free software; you can redistribute it and/or
* modify it under the terms of the license at
* http://www.jmanage.org.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.jmanage.core.alert.source;

import org.jmanage.core.config.AlertSourceConfig;
import org.jmanage.core.management.*;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.alert.AlertInfo;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class NotificationAlertSource extends MBeanAlertSource {

    private static final Logger logger =
            Loggers.getLogger(NotificationAlertSource.class);

    private ObjectNotificationListener listener;
    private ObjectNotificationFilterSupport filter;

    public NotificationAlertSource(AlertSourceConfig sourceConfig){
        super(sourceConfig);
    }

    protected void registerInternal() {

        /* start looking for this notification */
        listener = new ObjectNotificationListener(){
            public void handleNotification(ObjectNotification notification,
                                           Object handback) {
                try {
                    NotificationAlertSource.this.handler.handle(
                            new AlertInfo(notification));
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error while handling alert", e);
                }
            }
        };
        filter = new ObjectNotificationFilterSupport();
        filter.enableType(sourceConfig.getNotificationType());
        connection.addNotificationListener(new ObjectName(sourceConfig.getObjectName()),
                listener, filter, null);
    }

    protected void unregisterInternal() {

        assert connection != null;

        try {
            /* remove notification listener */
            connection.removeNotificationListener(
                    new ObjectName(sourceConfig.getObjectName()),
                    listener, filter, null);
        } catch (Exception e) {
            logger.log(Level.WARNING,
                    "Error while Removing Notification Listener. error: " +
                    e.getMessage());
        }

        listener = null;
        filter = null;
    }
}
