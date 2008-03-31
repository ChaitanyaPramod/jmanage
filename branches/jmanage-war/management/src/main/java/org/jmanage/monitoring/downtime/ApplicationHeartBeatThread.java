/**
 * Copyright 2004-2006 jManage.org
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
package org.jmanage.monitoring.downtime;

import java.io.IOException;
import java.util.logging.Logger;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.event.ApplicationEvent;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ServerConnector;
import org.jmanage.core.util.Loggers;
import org.jmanage.event.EventSystem;
import org.jmanage.monitoring.data.collector.DataCollector;
import org.jmanage.monitoring.downtime.event.ApplicationDownEvent;
import org.jmanage.monitoring.downtime.event.ApplicationUpEvent;

/**
 * ApplicationHeartBeatThread tracks downtime of a single application.
 * This information is captured as a list of ApplicationDowntime objects.
 * 
 * @author Rakesh Kalra
 */
public class ApplicationHeartBeatThread extends Thread {

    private static final Logger logger = Loggers
            .getLogger(ApplicationHeartBeatThread.class);

    private final ApplicationConfig appConfig;

    private boolean end = false;

    private boolean wasOpen = true;

    protected ApplicationHeartBeatThread(ApplicationConfig appConfig) {
        super("ApplicationHeartBeatThread:" + appConfig.getName());
        this.appConfig = appConfig;
    }

    protected ApplicationHeartBeatThread(ApplicationEvent appEvent) {
        this(appEvent.getApplicationConfig());
        /* check if the application is up */
        wasOpen = isOpen();
        if(!wasOpen){
            EventSystem.getInstance().fireEvent(
                    new ApplicationDownEvent(appConfig, appEvent.getTime()));    
        }
    }
    
    @Override
    public void run() {
        logger.info("Thread started: " + this.getName());
        while (!end) {
            checkApplicationStatus();
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                logger.warning("InterruptedException: " + e.getMessage());
            }
        }
        logger.info("Thread finished: " + this.getName());
    }

    protected void end() {
        end = true;
    }

    private void checkApplicationStatus() {
        final boolean isOpen = isOpen();
        if(wasOpen && !isOpen){
            // application went down
            wasOpen = false;
            EventSystem.getInstance().fireEvent(
                    new ApplicationDownEvent(appConfig));
        }else if(!wasOpen && isOpen){
            // application came pack up
            wasOpen = true;
            EventSystem.getInstance().fireEvent(
                    new ApplicationUpEvent(appConfig));
        }
    }
    
    private boolean isOpen(){
        ServerConnection connection = null;
        try {
            connection = ServerConnector.getServerConnection(appConfig);
            boolean isOpen = connection.isOpen();
            if(isOpen){
            	/* call the data collector to get data for configured attributes */
            	DataCollector.collect(appConfig, connection);
            }
            return isOpen;
        }catch(Exception e){
            logger.info("Application is down: " + appConfig.getName());
            return false;
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        }
    }

    public ApplicationConfig getApplicationConfig() {
        return appConfig;
    }
}
