package org.jmanage.testapp.mbeans;

import javax.management.NotificationBroadcasterSupport;
import javax.management.MBeanNotificationInfo;

/**
 *
 * date:  Dec 23, 2004
 * @author	Vandana Taneja
 */
public class TimeNotificationBroadcaster extends NotificationBroadcasterSupport
        implements TimeNotificationBroadcasterMBean {

    public MBeanNotificationInfo[] getNotificationInfo(){

        String type = "time.expired";
        MBeanNotificationInfo[] notifications = new MBeanNotificationInfo[1];
        notifications[0] = new MBeanNotificationInfo(new String[]{type},
                "Notification Types",
                "Types of notifications emitted by this broadcaster");

        return notifications;
    }





}
