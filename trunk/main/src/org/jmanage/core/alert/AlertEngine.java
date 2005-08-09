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

import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.AlertConfig;
import org.jmanage.core.util.Loggers;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * todo: AlertEngine should be notified when alert configurations are modified
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class AlertEngine {

    private static final Logger logger = Loggers.getLogger(AlertEngine.class);

    private static final AlertEngine alertEngine = new AlertEngine();

    public static AlertEngine getInstance(){
        return alertEngine;
    }

    private List alerts = Collections.synchronizedList(new LinkedList());

    private AlertEngine(){}

    public void start(){
        List alertConfigs = ApplicationConfigManager.getAllAlerts();
        for(Iterator it=alertConfigs.iterator(); it.hasNext();){
            AlertConfig alertConfig = (AlertConfig)it.next();
            Alert alert = new Alert(alertConfig);
            alert.register();
        }
        logger.info("AlertEngine started.");
    }

    public void stop(){
        for(Iterator it=alerts.iterator(); it.hasNext(); ){
            Alert alert = (Alert)it.next();
            alert.unregister();
        }
        // remove all alerts
        alerts.clear();
        logger.info("AlertEngine stopped.");
    }

    public synchronized void updateAlertConfig(AlertConfig alertConfig) {
        Alert alert = new Alert(alertConfig);
        int index = alerts.indexOf(alert);
        if(index != -1){
            // its an existing alert
            Alert oldAlert = (Alert)alerts.remove(index);
            oldAlert.unregister();
        }
        // register the alert
        alert.register();
        // add it to the list
        alerts.add(alert);
    }
}
