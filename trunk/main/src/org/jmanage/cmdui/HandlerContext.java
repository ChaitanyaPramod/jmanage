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

import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.services.ServiceContextImpl;
import org.jmanage.core.auth.User;
import org.jmanage.core.crypto.Crypto;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class HandlerContext {

    private final Command command;
    private final ServiceContextImpl serviceContext;

    HandlerContext(Command command){
        this(command, true);
    }

    HandlerContext(Command command, boolean isAuthRequired){
        this.command = command;
        this.serviceContext = getServiceContext(command, isAuthRequired);
    }

    public Command getCommand(){
        return command;
    }

    public ServiceContext getServiceContext(){
        return serviceContext;
    }

    public ServiceContext getServiceContext(String appName){
        assert appName != null;
        ServiceContextImpl serviceContext = getServiceContext(command, true);
        serviceContext.setApplicationName(appName);
        return serviceContext;
    }

    public ServiceContext getServiceContext(String appName, String mbeanName) {
        assert mbeanName != null;
        ServiceContextImpl serviceContext =
                (ServiceContextImpl)getServiceContext(appName);
        serviceContext.setMBeanName(mbeanName);
        return serviceContext;
    }

    private static ServiceContextImpl getServiceContext(Command command,
                                                        boolean isAuthRequired){
        ServiceContextImpl context = new ServiceContextImpl();
        if(isAuthRequired){
            assert command.getUsername() != null;
            assert command.getPassword() != null;
            User user = new User(command.getUsername(),
                    Crypto.hash(command.getPassword()), null, null, 0);
            context.setUser(user);
        }
        return context;
    }
}
