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
package org.jmanage.core.remote.client;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.jmanage.core.config.JManageProperties;
import org.jmanage.core.remote.Marshaller;
import org.jmanage.core.remote.Unmarshaller;
import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.services.ServiceException;
import org.jmanage.core.util.Loggers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

/**
 * Acts as a Proxy for Service layer method calls. This is used by the
 * ServiceFactory when the factory runs in the Remote mode.
 *
 * @see org.jmanage.core.remote.server.ServiceCallHandler
 * date:  Jan 17, 2005
 * @author	Rakesh Kalra
 */
public class ServiceProxy  implements InvocationHandler {

    private static final Logger logger = Loggers.getLogger(ServiceProxy.class);

    public static final String CLASS_METHOD_DELIMITER = "?";

    private static final String url = "http://" +
            JManageProperties.getHostName() + ":" +
            JManageProperties.getPort() + "/remoteService.do";

    private static final XmlRpcClient client;

    static{
        try {
            client = new XmlRpcClient(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        final String methodName = getMethodName(method);
        logger.log(Level.FINE, "Invoking service method: {0}", methodName);

        Object result = null;
        try {
            String output = (String)client.execute(methodName, toVector(method, args));
            /* convert output XML to Object */
            result = null;
            if(output != null){
                result = Unmarshaller.unmarshal(method.getReturnType(), output);
            }
        } catch (XmlRpcException e) {
            logger.log(Level.FINE, "XmlRpcException", e);
            throw new ServiceException("", e.getMessage());
        }
        return result;
    }

    private static String getMethodName(Method method){
        return method.getDeclaringClass().getName() + CLASS_METHOD_DELIMITER +
                method.getName();
    }

    /**
     * The first element in the vector is a list of class names.
     * The first argument to the service method call is always an instance
     * of ServiceContext.
     */
    private static Vector toVector(Method method, Object[] args){
        Vector vector = new Vector(args.length+1);
        /* add list of class names */
        vector.add(getClassNames(method));
        for(int i=0; i<args.length; i++){
            vector.add(Marshaller.marshal(args[i]));
        }
        return vector;
    }

    private static Vector getClassNames(Method method){
        Class[] parameterTypes = method.getParameterTypes();
        assert parameterTypes.length > 0 &&
                (ServiceContext.class.isAssignableFrom(parameterTypes[0])) :
                "Invalid service method. First argument must be ServiceContext";
        Vector vector = new Vector(parameterTypes.length);
        for(int i=0; i<parameterTypes.length; i++){
            vector.add(parameterTypes[i].getName());
        }
        return vector;
    }
}
