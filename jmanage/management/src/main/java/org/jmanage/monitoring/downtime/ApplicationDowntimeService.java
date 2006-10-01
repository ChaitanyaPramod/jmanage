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

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.util.Loggers;
import org.jmanage.monitoring.downtime.event.Event;
import org.jmanage.monitoring.downtime.event.EventListener;

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
    
    private final List<EventListener> eventListeners = new LinkedList<EventListener>();
    private final List<ApplicationDowntimeTrackingThread> threads = 
        new LinkedList<ApplicationDowntimeTrackingThread>();
    private final DowntimeRecorder recorder = DowntimeRecorder.getInstance();
    
    private ApplicationDowntimeService(){
    }
    
    public void start() {
        // TODO: threads for new applications are not getting started
        for (ApplicationConfig appConfig : ApplicationConfigManager
                .getAllApplications()) {
            ApplicationDowntimeTrackingThread thread = 
                new ApplicationDowntimeTrackingThread(appConfig);
            threads.add(thread);
            thread.start();
        }
        addListener(recorder);
        logger.info("ApplicationDowntimeService started.");
    }

    public void stop() {
        for(ApplicationDowntimeTrackingThread thread: threads){
            thread.end();
        }
        threads.clear();
        eventListeners.clear();
    }

    public void addListener(EventListener listener) {
        eventListeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public DowntimeRecorder getDowntimeRecorder(){
        return recorder;
    }
    
    void fireEvent(Event event){
        for(EventListener listener:eventListeners){
            try{
                listener.handleEvent(event);
            }catch(Throwable t){
                logger.log(Level.SEVERE, "Error in event listener", t);
            }
        }
    }
}
