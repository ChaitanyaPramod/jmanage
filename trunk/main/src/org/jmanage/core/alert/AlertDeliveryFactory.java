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
import org.jmanage.core.config.AlertDeliveryConstants;

import java.util.List;
import java.util.ArrayList;

/**
 *
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class AlertDeliveryFactory {

    // todo: it will be good to allow users to create custom delivery types
    public static AlertDelivery getAlertDelivery(String deliveryType,
                                                 AlertConfig alertConfig){
        if(deliveryType.equals(AlertDeliveryConstants.EMAIL_ALERT_DELIVERY_TYPE)){
            return new EmailDelivery(alertConfig);
        }else if(deliveryType.equals(AlertDeliveryConstants.CONSOLE_ALERT_DELIVERY_TYPE)){
            return new ConsoleDelivery(alertConfig);
        }else{
            throw new RuntimeException("Invalid deliveryType=" + deliveryType);
        }
    }

    public static List getAlertDeliveries(AlertConfig alertConfig) {
        String[] deliveryTypes = alertConfig.getAlertDelivery();
        List deliveries = new ArrayList(deliveryTypes.length);
        for(int i=0; i < deliveryTypes.length; i++){
            deliveries.add(getAlertDelivery(deliveryTypes[i], alertConfig));
        }
        return deliveries;
    }
}
