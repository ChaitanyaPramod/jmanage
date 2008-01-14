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
package org.jmanage.cmdui;

import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.services.ConfigurationService;
import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.services.ServiceContextImpl;
import org.jmanage.core.auth.User;
import org.jmanage.core.data.ApplicationConfigData;

/**
 *
 * date:  Jan 19, 2005
 * @author	Rakesh Kalra
 */
public class CommandInterface {

    static{
        /* initialize ServiceFactory */
        ServiceFactory.init(ServiceFactory.MODE_REMOTE);
    }

    public static void main(String[] args){
        ConfigurationService configService =
                ServiceFactory.getConfigurationService();

        ApplicationConfigData configData = new ApplicationConfigData();
        configData.setHost("localhost");
        configData.setPort(new Integer(7001));
        configData.setName("testApp");
        configData.setType("weblogic");
        configData.setUsername("system");
        configData.setPassword("12345678");

        configData = configService.addApplication(getServiceContext(),
                configData);
        System.out.println("ApplicationId:" + configData.getApplicationId());
        System.out.println("Host:" + configData.getHost());
        System.out.println("Port:" + configData.getPort());
        System.out.println("Name:" + configData.getName());
        System.out.println("Type:" + configData.getType());
        System.out.println("Username:" + configData.getUsername());
        System.out.println("Password:" + configData.getPassword());
    }

    private static ServiceContext getServiceContext(){
        ServiceContextImpl context = new ServiceContextImpl();
        User user = new User("admin", null, null, null, 0);
        context.setUser(user);
        return context;
    }
}
