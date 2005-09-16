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

/**
 *
 * Date:  Jul 1, 2005
 * @author	Rakesh Kalra
 */
public abstract class AlertSource {

    protected final AlertSourceConfig sourceConfig;

    public AlertSource(AlertSourceConfig sourceConfig){
        assert sourceConfig != null;
        this.sourceConfig = sourceConfig;
    }

    public abstract void register(AlertHandler handler,
                                  String alertId,
                                  String alertName);

    public abstract void unregister();
}
