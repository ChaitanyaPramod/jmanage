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
package org.jmanage.webui.util;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.auth.User;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ServerConnector;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.MalformedObjectNameException;
import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.services.ServiceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class WebContext {

    private ApplicationConfig appConfig;
    private HttpServletRequest request;
    private HttpSession session;
    private ServiceContext serviceContext;

    private ServerConnection serverConnection;

    private WebContext(HttpServletRequest request) {

        this.request = request;
        this.session = request.getSession();
        final String appId = request.getParameter(RequestParams.APPLICATION_ID);
        if (appId != null) {
            appConfig =
                    ApplicationConfigManager.getApplicationConfig(appId);
            request.setAttribute(RequestAttributes.APPLICATION_CONFIG,
                    appConfig);
        }
    }

    public ApplicationConfig getApplicationConfig() {
        return appConfig;
    }

    public ServerConnection getServerConnection(){
        assert appConfig != null;
        assert !appConfig.isCluster():"not supported for cluster";
        if (serverConnection == null) {
            serverConnection =
                    ServerConnector.getServerConnection(appConfig);
        }
        return serverConnection;
    }

    public ObjectName getObjectName() {
         return getObjectName(request);
    }

    public static ObjectName getObjectName(HttpServletRequest request){
        try {
            final String objectNameString =
                    request.getParameter(RequestParams.OBJECT_NAME);
            if(objectNameString != null){
                return new ObjectName(objectNameString);
            }else{
                return null;
            }
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    public static WebContext get(HttpServletRequest request) {
        WebContext context =
                (WebContext) request.getAttribute(RequestAttributes.WEB_CONTEXT);
        if (context == null) {
            context = new WebContext(request);
            request.setAttribute(RequestAttributes.WEB_CONTEXT, context);
        }
        return context;
    }

    public void setUser(User user) {
        session.setAttribute(RequestAttributes.AUTHENTICATED_USER, user);
    }

    public void removeUser() {
        session.removeAttribute(RequestAttributes.AUTHENTICATED_USER);
    }

    public User getUser(){
        return (User)session.getAttribute(RequestAttributes.AUTHENTICATED_USER);
    }

    public ServiceContext getServiceContext(){
        if(serviceContext == null){
            serviceContext = new ServiceContextImpl(this);
        }
        return serviceContext;
    }

    public void releaseResources() {
        ServiceUtils.close(serverConnection);
        serverConnection = null;
    }
}
