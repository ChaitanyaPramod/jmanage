package org.jmanage.webui.util;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.connector.MBeanServerConnectionFactory;
import org.jmanage.core.connector.ConnectionFailedException;

import javax.servlet.http.HttpServletRequest;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

/**
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra
 */
public class WebContext {

    private ApplicationConfig appConfig;
    private MBeanServer mbeanServer;
    private HttpServletRequest request;

    private WebContext(HttpServletRequest request){

        this.request = request;
        final String appId = request.getParameter(RequestParams.APPLICATION_ID);
        if(appId != null){
            appConfig =
                    ApplicationConfigManager.getApplicationConfig(appId);
            request.setAttribute(RequestAttributes.APPLICATION_CONFIG,
                    appConfig);
        }
    }

    public ApplicationConfig getApplicationConfig(){
        return appConfig;
    }

    public MBeanServer getMBeanServer(){
        //assert appConfig != null; TODO: assert
        if(mbeanServer == null){
            try {
                mbeanServer =
                        MBeanServerConnectionFactory.getConnection(appConfig);
            } catch (ConnectionFailedException e) {
                throw new RuntimeException("Connection failed for config=" +
                        appConfig, e);
            }
        }
        return mbeanServer;
    }

    public ObjectName getObjectName() {
        try {
            final String objectNameString =
                    request.getParameter(RequestParams.OBJECT_NAME);
            return new ObjectName(objectNameString);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    public static WebContext get(HttpServletRequest request) {
        WebContext context =
                (WebContext)request.getAttribute(RequestAttributes.WEB_CONTEXT);
        if(context == null){
            context = new WebContext(request);
            request.setAttribute(RequestAttributes.WEB_CONTEXT, context);
        }
        return context;
    }
}
