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
package org.jmanage.core.modules.snmp;

import org.jmanage.core.config.ApplicationConfig;

/**
 * @author shashank
 * Date: Jul 31, 2005
 */
public class SNMPApplicationConfig extends ApplicationConfig{

    public SNMPApplicationConfig(){
        super();
        setPort(new Integer(161));
    }

    public String getURL() {
        return "";
    }

}
