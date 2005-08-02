/**
 * Copyright (c) 2004-2005 jManage.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.jmanage.core.alert;

import org.jmanage.core.config.AlertSourceConfig;
import org.jmanage.core.management.*;
import org.jmanage.core.util.Loggers;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class NotificationAlertSource extends AlertSource {

    private static final Logger logger =
            Loggers.getLogger(NotificationAlertSource.class);

    private AlertHandler handler;
    private ServerConnection connection;

    public NotificationAlertSource(AlertSourceConfig sourceConfig){
        super(sourceConfig);
    }

    public void register(AlertHandler handler) {
        this.handler = handler;

        /* start looking for this notification */
        connection = ServerConnector.getServerConnection(
                sourceConfig.getApplicationConfig());
        ObjectNotificationListener listener = new ObjectNotificationListener(){
            public void handleNotification(ObjectNotification notification,
                                           Object handback) {
                try {
                    NotificationAlertSource.this.handler.handle(new AlertInfo(notification));
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error while handling alert", e);
                }
            }
        };
        ObjectNotificationFilterSupport filter = new ObjectNotificationFilterSupport();
        filter.enableType(sourceConfig.getNotificationType());
        connection.addNotificationListener(sourceConfig.getObjectName(),
                listener, filter, null);
    }

    public void unregister() {
        this.handler = null;
        /* todo: stop looking for this notification*/
    }
}
