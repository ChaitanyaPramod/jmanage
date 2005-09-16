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
package org.jmanage.core.alert.source;

import org.jmanage.core.config.AlertSourceConfig;
import org.jmanage.core.management.*;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.alert.AlertSource;
import org.jmanage.core.alert.AlertHandler;
import org.jmanage.core.alert.AlertInfo;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

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
    private ObjectNotificationListener listener;
    private ObjectNotificationFilterSupport filter;

    public NotificationAlertSource(AlertSourceConfig sourceConfig){
        super(sourceConfig);
    }

    public void register(AlertHandler handler,
                         String alertId,
                         String alertName) {
        this.handler = handler;

        /* start looking for this notification */
        connection = ServerConnector.getServerConnection(
                sourceConfig.getApplicationConfig());
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

    public void unregister() {

        this.handler = null;
        assert connection != null;

        /* remove notification listener */
        connection.removeNotificationListener(
                new ObjectName(sourceConfig.getObjectName()),
                listener, filter, null);

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
