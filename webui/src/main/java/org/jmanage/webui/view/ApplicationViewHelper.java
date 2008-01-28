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
package org.jmanage.webui.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.monitoring.downtime.ApplicationDowntimeHistory;
import org.jmanage.monitoring.downtime.ApplicationDowntimeService;
import org.jmanage.monitoring.downtime.DowntimeRecorder;

/**
 * 
 * @author Rakesh Kalra
 */
public class ApplicationViewHelper {
    
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm aaa"); 

    public static boolean isApplicationUp(ApplicationConfig appConfig) {
        DowntimeRecorder recorder = ApplicationDowntimeService.getInstance().getDowntimeRecorder();
        boolean isUp = true;
        if (appConfig.isCluster()) {
            for (ApplicationConfig childAppConfig : appConfig.getApplications()) {
                if (!recorder.isApplicationUp(childAppConfig)) {
                    // once an application is detected that is down, there is no need to proceed
                    // further
                    isUp = false;
                    break;
                }
            }
        }
        else {
            isUp = recorder.isApplicationUp(appConfig);
        }
        return isUp;
    }
    
    public static String getRecordingSince(ApplicationConfig appConfig){
        DowntimeRecorder recorder = ApplicationDowntimeService.getInstance().getDowntimeRecorder();
        ApplicationDowntimeHistory history = recorder.getDowntimeHistory(appConfig);
        return formatter.format(new Date(history.getRecordingSince()));
    }
}
