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

import org.apache.xmlrpc.XmlRpcHandler;
import org.jmanage.core.remote.client.ServiceProxy;
import org.jmanage.core.remote.Unmarshaller;
import org.jmanage.core.remote.Marshaller;
import org.jmanage.core.services.ServiceContextImpl;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.services.AuthService;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.User;
import org.jmanage.core.util.Loggers;

import java.util.Vector;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.lang.reflect.Method;

/**
 * ServiceCallHandler is a XMLRPC handler, which handles remote requests
 * for executing Service layer methods. These remote requests originate from
 * ServiceProxy.
 * <p>
 * There are certain assumptions being made for the Service layer methods:
 * <ul>
 * <li>All service layer methods take ServiceContext as the first argument.
 * <li>Service layer method arguments follow the JavaBean convention.
 * <li>Service layer returns objects that follow the JavaBean convention.
 * </ul>
 *
 * @see org.jmanage.core.remote.client.ServiceProxy
 * date:  Jan 17, 2005
 * @author	Rakesh Kalra
 */
public class ServiceCallHandler implements XmlRpcHandler {

    private static final Logger logger =
            Loggers.getLogger(ServiceCallHandler.class);

    public Object execute(String serviceMethod, Vector parameters)
            throws Exception {

        try {
            int index = serviceMethod.indexOf(ServiceProxy.CLASS_METHOD_DELIMITER);
            final String className = serviceMethod.substring(0, index);
            final String methodName = serviceMethod.substring(index + 1);
            Class serviceClass = Class.forName(className);
            Class[] parameterTypes = getParameterTypes((List)parameters.get(0));
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            Object serviceObject = ServiceFactory.getService(serviceClass);
            Object[] args = toObjectArray(parameters, parameterTypes);
            /* authenticate the request */
            authenticate((ServiceContextImpl)args[0], className, methodName);
            /* invoke the method */
            Object result =
                    method.invoke(serviceObject, args);
            return Marshaller.marshal(result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while invoking: " +
                    serviceMethod, e);
            throw e;
        }
    }

    private static Class[] getParameterTypes(List classNames)
        throws ClassNotFoundException {

        Class[] parameterTypes = new Class[classNames.size()];
        int i=0;
        for(Iterator it=classNames.iterator(); it.hasNext();){
            String className = (String)it.next();
            parameterTypes[i++] = Class.forName(className);
        }
        return parameterTypes;
    }

    private static Object[] toObjectArray(Vector parameters,
                                          Class[] parameterTypes){
        Object[] args = new Object[parameterTypes.length];
        for(int i=0; i<parameterTypes.length; i++){
            if(i == 0){
                /* first argument must always be ServiceContext */
                assert parameterTypes[i].equals(ServiceContext.class):
                        "First argument to the service method must be " +
                        "ServiceContext ";
                ServiceContextImpl serviceContext =
                        (ServiceContextImpl)Unmarshaller.unmarshal(
                                ServiceContextImpl.class,
                                (String)parameters.get(i+1));
                args[i] = serviceContext;
            }else{
                args[i] = Unmarshaller.unmarshal(parameterTypes[i],
                        (String)parameters.get(i+1));
            }
        }
        return args;
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
