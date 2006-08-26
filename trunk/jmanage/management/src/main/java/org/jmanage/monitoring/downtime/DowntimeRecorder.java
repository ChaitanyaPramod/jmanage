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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.monitoring.downtime.event.ApplicationDownEvent;
import org.jmanage.monitoring.downtime.event.ApplicationUpEvent;
import org.jmanage.monitoring.downtime.event.Event;
import org.jmanage.monitoring.downtime.event.EventListener;

/**
 * 
 * @author Rakesh Kalra
 */
public class DowntimeRecorder implements EventListener {

    private final Map<ApplicationConfig, ApplicationDowntimeHistory> downtimesMap = 
        new HashMap<ApplicationConfig, ApplicationDowntimeHistory>();
        
    DowntimeRecorder(){
        final long recordingSince = System.currentTimeMillis();
        for (ApplicationConfig appConfig : ApplicationConfigManager.getAllApplications()) {
            downtimesMap.put(appConfig, new ApplicationDowntimeHistory(recordingSince));
        }
    }
    
    public ApplicationDowntimeHistory getDowntimeHistory(ApplicationConfig appConfig){
        return downtimesMap.get(appConfig);
    }
    
    public void handleEvent(Event event) {
        ApplicationDowntimeHistory downtimeHistory = downtimesMap.get(event.getApplicationConfig());
        assert downtimeHistory != null;
        List<ApplicationDowntime> downtimes = downtimeHistory.getDowntimes();
        if(event instanceof ApplicationUpEvent){
            // application must have went down earlier
            assert downtimes.size() > 0;
            ApplicationDowntime lastDowntime = downtimes.get(downtimes.size() - 1);
            assert lastDowntime.getEndTime() == null;
            lastDowntime.setEndTime(event.getTime());
        }else if(event instanceof ApplicationDownEvent){
            if(downtimes.size() > 0){
                ApplicationDowntime lastDowntime = downtimes.get(downtimes.size() - 1);
                assert lastDowntime.getEndTime() != null;
            }
            ApplicationDowntime downtime = new ApplicationDowntime();
            downtime.setStartTime(event.getTime());
            downtimeHistory.addDowntime(downtime);
        }
    }

    public double getUnavailablePercentage(ApplicationConfig appConfig) {
        final ApplicationDowntimeHistory history = downtimesMap.get(appConfig);
        long totalRecordingTime = System.currentTimeMillis() - history.getRecordingSince();
        long totalDowntime = computeTotalDowntime(history.getDowntimes());
        return (totalDowntime * 100.0d)/totalRecordingTime; 
    }

    private long computeTotalDowntime(List<ApplicationDowntime> downtimes) {
        long totalDowntime = 0;
        for(ApplicationDowntime downtime:downtimes){
            totalDowntime += downtime.getTime();
        }
        return totalDowntime;
    }
}
