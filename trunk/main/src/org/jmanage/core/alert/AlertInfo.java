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

import org.jmanage.core.management.ObjectNotification;

/**
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class AlertInfo {

    private final ObjectNotification notification;

    public AlertInfo(ObjectNotification notification){
        this.notification = notification;
    }

    public String getType() {
        return notification.getType();
    }

    public long getSequenceNumber() {
        return notification.getSequenceNumber();
    }

    public String getMessage() {
        return notification.getMessage();
    }

    public long getTimeStamp() {
        return notification.getTimeStamp();
    }

    public Object getUserData() {
        return notification.getUserData();
    }

    public Object getMySource() {
        return notification.getMySource();
    }

}
