/**
 * jManage - Open Source Application Management
 * Copyright (C) 2004-2006 jManage.org
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
package org.jmanage.core.services;

import org.jmanage.core.data.ApplicationConfigData;
import org.jmanage.core.services.ConfigurationService;
import org.jmanage.core.services.RemoteServiceContextFactory;
import org.jmanage.core.services.ServiceContext;

/**
 * Simple test for the remote API.
 * TODO: This needs to be converted to a JUnit test. -rk
 * 
 * @author rkalra
 */
public class RemoteServiceContextFactoryTest {

    public static void main(String[] args) {

        RemoteServiceContextFactory.setJManageURL("http://localhost:9090");
        ServiceContext context =
                RemoteServiceContextFactory.getServiceContext("admin", "123456", null, null);
        ConfigurationService configService = ServiceFactory.getConfigurationService();

        /* Build ApplicationConfigData object */
        ApplicationConfigData configData = new ApplicationConfigData();
        configData.setHost("intadui-001.ysm.oc2.yahoo.com");
        configData.setPort(new Integer(1099));
        configData.setName("testApp1");
        configData.setType("jboss");
        /* add application */
        configData = configService.addApplication(context, configData);
        System.out.println("ApplicationId:" + configData.getApplicationId());
    }
}
