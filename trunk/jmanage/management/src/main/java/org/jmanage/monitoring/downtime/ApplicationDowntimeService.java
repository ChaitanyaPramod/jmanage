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

import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.event.ApplicationChangedEvent;
import org.jmanage.core.config.event.ApplicationEvent;
import org.jmanage.core.config.event.NewApplicationEvent;
import org.jmanage.core.util.Loggers;
import org.jmanage.event.EventListener;
import org.jmanage.event.EventSystem;

/**
 * This service acts as the facade to the downtime tracking sub-system in
 * jManage. It exposes start() and stop() methods to manage it's lifecycle.
 * 
 * @author Rakesh Kalra
 */
public class ApplicationDowntimeService {

    private static final Logger logger = Loggers.getLogger(ApplicationDowntimeService.class);
    
    private static final ApplicationDowntimeService service = new ApplicationDowntimeService();
    
    public static ApplicationDowntimeService getInstance(){
        return service;
    }
    
    private final List<ApplicationHeartBeatThread> threads = 
        new LinkedList<ApplicationHeartBeatThread>();
    private final DowntimeRecorder recorder = DowntimeRecorder.getInstance();
    
    private ApplicationDowntimeService(){
    }
    
    public void start() {

        for (ApplicationConfig appConfig : ApplicationConfigManager
                .getAllApplications()) {
            // only add non-cluster applications
            if(!appConfig.isCluster())
                addApplication(appConfig);
        }
        
        // TODO: perfect dependency to be injected via Spring framework --rk
        EventSystem eventSystem = EventSystem.getInstance();
         
        /* Add the recorder to record the downtimes to the DB */
        eventSystem.addListener(recorder, ApplicationEvent.class);

        /* application event listener to add */
        eventSystem.addListener(new EventListener(){
            public void handleEvent(EventObject event) {
                if(!(event instanceof ApplicationEvent)){
                    throw new IllegalArgumentException("event must be of type ApplicationEvent");
                }
                if(event instanceof NewApplicationEvent){
                    addApplication(((NewApplicationEvent)event).getApplicationConfig());
                }else if(event instanceof ApplicationChangedEvent){
                    applicationChanged(((ApplicationChangedEvent)event).getApplicationConfig());
                }
            }
        }, ApplicationEvent.class);
        
        logger.info("ApplicationDowntimeService started.");
    }

    public void stop() {
        for(ApplicationHeartBeatThread thread: threads){
            thread.end();
        }
        threads.clear();
    }

    public DowntimeRecorder getDowntimeRecorder(){
        return recorder;
    }
    
    private void addApplication(ApplicationConfig appConfig) {
        ApplicationHeartBeatThread thread = 
            new ApplicationHeartBeatThread(appConfig);
        threads.add(thread);
        thread.start();
    }

    private void applicationChanged(ApplicationConfig appConfig) {
        ApplicationHeartBeatThread associatedThread = null;
        for(ApplicationHeartBeatThread thread:threads){
            if(thread.getApplicationConfig().equals(appConfig)){
                associatedThread = thread;
                break;
            }
        }
        if(associatedThread == null){
            logger.log(Level.WARNING, "Thread not found for application: {0}", appConfig);
        }else{
            threads.remove(associatedThread);
            addApplication(appConfig);
        }
    }
}
