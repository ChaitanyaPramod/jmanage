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
package org.jmanage.core.services;

import org.jmanage.core.alert.delivery.ConsoleAlerts;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.data.AlertData;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 *
 * Date:  Aug 3, 2005
 * @author	Rakesh Kalra
 */
public class AlertServiceImpl implements AlertService {

    public List getConsoleAlerts(ServiceContext context)
            throws ServiceException {

        List alerts = new LinkedList();
        // convert to a list of AlertInfo objects into a list of AlertData objects
        for(Iterator it=ConsoleAlerts.getAll().iterator(); it.hasNext();){
            AlertData alertData = new AlertData();
            CoreUtils.copyProperties(alertData, it.next());
            alerts.add(alertData);
        }
        return alerts;
    }

    public void removeConsoleAlert(ServiceContext context,
                                   String alertId) {
        ConsoleAlerts.remove(alertId);
    }
}
