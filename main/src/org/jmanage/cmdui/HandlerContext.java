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
    private final ServiceContext serviceContext;

    HandlerContext(Command command){
        this.command = command;
        this.serviceContext = getServiceContext(command);
    }

    public Command getCommand(){
        return command;
    }

    public ServiceContext getServiceContext(){
        return serviceContext;
    }

    private static ServiceContext getServiceContext(Command command){
        assert command.getUsername() != null;
        assert command.getPassword() != null;
        ServiceContextImpl context = new ServiceContextImpl();
        User user = new User(command.getUsername(),
                Crypto.hash(command.getPassword()), null, null, 0);
        context.setUser(user);
        return context;
    }
}
