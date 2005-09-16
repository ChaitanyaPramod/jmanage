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
import org.jmanage.core.alert.source.NotificationAlertSource;
import org.jmanage.core.alert.source.GaugeAlertSource;
import org.jmanage.core.alert.source.StringAlertSource;

/**
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class AlertSourceFactory {

    public static AlertSource getAlertSource(AlertSourceConfig sourceConfig) {
        final String sourceType = sourceConfig.getSourceType();
        if(sourceType.equals(AlertSourceConfig.SOURCE_TYPE_NOTIFICATION)){
            return new NotificationAlertSource(sourceConfig);
        }else if(sourceType.equals(AlertSourceConfig.SOURCE_TYPE_GAUGE_MONITOR)){
            return new GaugeAlertSource(sourceConfig);
        }else if(sourceType.equals(AlertSourceConfig.SOURCE_TYPE_STRING_MONITOR)){
            return new StringAlertSource(sourceConfig);
        }
        assert false:"Unknown alert source type: " + sourceType;
        return null;
    }
}
