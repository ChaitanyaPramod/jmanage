package org.jmanage.core.remote.client;

import org.apache.xmlrpc.XmlRpcClient;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.remote.Unmarshaller;
import org.jmanage.core.remote.Marshaller;
import org.jmanage.core.config.JManageProperties;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.StringReader;

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
        String output = (String)client.execute(methodName, toVector(method, args));
        /* convert output XML to Object */
        Object result = null;
        if(output != null){
            result = Unmarshaller.unmarshal(method.getReturnType(), output);
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
     *
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
        Vector vector = new Vector(parameterTypes.length);
        for(int i=0; i<parameterTypes.length; i++){
            vector.add(parameterTypes[i].getName());
        }
        return vector;
    }
}
