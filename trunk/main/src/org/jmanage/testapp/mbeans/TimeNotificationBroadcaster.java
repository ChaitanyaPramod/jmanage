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
