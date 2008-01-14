/**
 * jManage - Open Source Application Management
 * Copyright (C) 2006 jManage.org.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License version 2 for more details.
 */
package org.jmanage.connector.plugin.oracle.mbean;

import java.util.logging.Logger;

import javax.management.openmbean.TabularData;

import org.jmanage.connector.plugin.oracle.DataAccessor;
import org.jmanage.core.util.Loggers;

/**
 * 
 * @author rkalra
 */
public class SGAStatistics {

    private static final Logger logger = Loggers.getLogger(SGAStatistics.class);
    
    private static final String SQL = "select POOL || ' - ' || NAME as NAME, BYTES from V$SGASTAT";
    
    public TabularData getStatistics(){
        try {
            return new DataAccessor(SQL).execute().getTabularData();
        }
        catch (Exception e) {
            logger.severe("Error getting SGA stats. error: " + e.getMessage());
            return null;
        }
    }
}
