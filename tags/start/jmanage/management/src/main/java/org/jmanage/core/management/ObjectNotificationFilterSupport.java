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
package org.jmanage.core.management;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class ObjectNotificationFilterSupport implements ObjectNotificationFilter{

    private Set notificationTypes = new HashSet();

    public void enableType(String type){
        assert type != null;
        notificationTypes.add(type);
    }

    /**
     * This method is called before a notification is sent to see whether
     * the listener wants the notification.
     *
     * @param notification the notification to be sent.
     * @return true if the listener wants the notification, false otherwise
     */
    public boolean isNotificationEnabled(ObjectNotification notification) {
        return notificationTypes.contains(notification.getType());
    }

    /**
     *
     * @return a list of notification types that are enabled.
     */
    public Collection getEnabledTypes() {
        return notificationTypes;
    }
}
