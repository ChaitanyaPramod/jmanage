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
package org.jmanage.core.remote.server;

import org.jmanage.core.auth.User;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.UnAuthorizedAccessException;
import org.jmanage.core.remote.InvocationResult;
import org.jmanage.core.remote.RemoteInvocation;
import org.jmanage.core.services.AuthService;
import org.jmanage.core.services.ServiceContextImpl;
import org.jmanage.core.services.ServiceException;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.util.ErrorCodes;
import org.jmanage.core.management.ConnectionFailedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ServiceCallHandler handles remote requests
 * for executing Service layer methods. These remote requests originate from
 * HttpServiceProxy.
 * <p>
 * There are certain assumptions being made for the Service layer methods:
 * <ul>
 * <li>All service layer methods take ServiceContext as the first argument.
 * </ul>
 *
 * @see org.jmanage.core.remote.client.HttpServiceProxy
 * date:  Jan 17, 2005
 * @author	Rakesh Kalra
 */
public class ServiceCallHandler {

    private static final Logger logger =
            Loggers.getLogger(ServiceCallHandler.class);

    public static InvocationResult execute(RemoteInvocation invocation){
        try {
            Object result = executeInternal(invocation.getClassName(),
                    invocation.getMethodName(),
                    invocation.getSignature(),
                    invocation.getArgs());
            return new InvocationResult(result);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if(t != null){
                if(t instanceof ServiceException || t instanceof UnAuthorizedAccessException){
                    return new InvocationResult(t);
                }else if(t instanceof ConnectionFailedException){
                    return new InvocationResult(
                            new ServiceException(ErrorCodes.CONNECTION_FAILED));
                }
            }
            logger.log(Level.SEVERE, "Error while invoking: " +
                    invocation.getClassName() +"->"+ invocation.getMethodName(),
                    e);
            throw new RuntimeException(e);
        } catch(Exception e){
            logger.log(Level.SEVERE, "Error while invoking: " +
                    invocation.getClassName() +"->"+ invocation.getMethodName(),
                    e);
            throw new RuntimeException(e);
        }
    }

    private static Object executeInternal(String className,
                                          String methodName,
                                          Class[] parameterTypes,
                                          Object[] args)
        throws Exception {

        /* authenticate the request */
        authenticate((ServiceContextImpl)args[0], className, methodName);
        /* invoke the method */
        Class serviceClass = Class.forName(className);
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        Object serviceObject = ServiceFactory.getService(serviceClass);
        /* invoke the method */
        return method.invoke(serviceObject, args);

    }

    private static void authenticate(ServiceContextImpl context,
                                     String className,
                                     String methodName){

        User user = context.getUser();
        if(user == null){
            /* only the login method in AuthService is allowed without
                authentication */
            if(!className.equals(AuthService.class.getName())
                    || !methodName.equals("login")){

                throw new RuntimeException("Service method called without " +
                        "User credentials");
            }
            return;
        }

        /* User must have username and password specified */
        assert user.getUsername() != null;
        assert user.getPassword() != null;

        UserManager userManager = UserManager.getInstance();
        User completeUser =
                userManager.getUser(user.getUsername());
        /* validate password */
        if(!user.getPassword().equals(completeUser.getPassword())
            || !"A".equals(completeUser.getStatus())){
            throw new RuntimeException("Invalid user credentials.");
        }

        context .setUser(completeUser);
    }
}
