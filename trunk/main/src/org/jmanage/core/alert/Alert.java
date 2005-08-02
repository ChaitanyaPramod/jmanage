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

import org.jmanage.core.config.AlertConfig;

import java.util.List;
import java.util.Iterator;

/**
 *
 * Date:  Jul 4, 2005
 * @author	Rakesh Kalra
 */
public class Alert implements AlertHandler {

    private final AlertConfig alertConfig;
    private final AlertSource source;
    private final List deliveries;

    public Alert(AlertConfig alertConfig){
        assert alertConfig != null;
        this.alertConfig = alertConfig;
        this.source = AlertSourceFactory.getAlertSource(
                alertConfig.getAlertSourceConfig());
        this.deliveries = AlertDeliveryFactory.getAlertDeliveries(alertConfig);
    }

    public void register(){
        source.register(this);
    }

    public void unregister(){
        source.unregister();
    }

    public void handle(AlertInfo alertInfo) {
        for(Iterator it=deliveries.iterator(); it.hasNext(); ){
            AlertDelivery delivery = (AlertDelivery)it.next();
            delivery.deliver(alertInfo);
        }
    }

    public int hashCode(){
        return alertConfig.getAlertId().hashCode();
    }

    public boolean equals(Object obj){
        if(obj instanceof Alert){
            Alert alert = (Alert)obj;
            return alert.alertConfig.getAlertId().equals(
                    alertConfig.getAlertId());
        }
        return false;
    }
}
