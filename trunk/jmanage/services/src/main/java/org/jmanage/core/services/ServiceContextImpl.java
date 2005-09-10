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
package org.jmanage.core.services;

import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.auth.User;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ServerConnector;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.util.CoreUtils;

import javax.security.auth.Subject;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * date:  Jan 19, 2005
 * @author	Rakesh Kalra
 * @author  Shashank Bellary
 */
public class ServiceContextImpl implements ServiceContext {

    private static final Logger logger =
            Loggers.getLogger(ServiceContextImpl.class);

    private User user;
    private String appName;
    private String mbeanName;

    private transient ServerConnection serverConnection;

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    /**
     * TODO webcontext should hold User instead of Subject.
     * TODO Add a _setUser(User user) method on webcontext and on ServiceContext.
     * TODO Then refactor to code to remove _setSubject(Subject subject) method.
     *
     * @param subject
     */
    public void _setSubject(Subject subject) {
    }

    public ApplicationConfig getApplicationConfig() {
        assert appName != null;
        return ServiceUtils.getApplicationConfigByName(appName);
    }

    public ObjectName getObjectName() {
        assert mbeanName != null;
        String mbeanName =
                ServiceUtils.resolveMBeanName(getApplicationConfig(),
                        this.mbeanName);
        return new ObjectName(mbeanName);
    }

    public ServerConnection getServerConnection() {
        ApplicationConfig appConfig = getApplicationConfig();
        assert appConfig != null;
        assert !appConfig.isCluster():"not supported for cluster";
        if (serverConnection == null) {
            serverConnection =
                    ServerConnector.getServerConnection(appConfig);
        }
        return serverConnection;
    }

    /**
     * @param appName configured application mame
     */
    public void setApplicationName(String appName) {
        this.appName = appName;
    }

    /**
     * @param mbeanName configured mbean name or object name
     */
    public void setMBeanName(String mbeanName){
        this.mbeanName =  mbeanName;
    }

    public void releaseResources() {
        ServiceUtils.close(serverConnection);
        serverConnection = null;
    }
}
