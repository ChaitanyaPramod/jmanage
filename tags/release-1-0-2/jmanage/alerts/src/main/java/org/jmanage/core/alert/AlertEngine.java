/**
* Copyright (c) 2004-2005 jManage.org
*
* This is a free software; you can redistribute it and/or
* modify it under the terms of the license at
* http://www.jmanage.org.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.jmanage.core.alert;

import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.AlertConfig;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.alert.delivery.EmailAlerts;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class AlertEngine {

    private static final Logger logger = Loggers.getLogger(AlertEngine.class);

    private static final AlertEngine alertEngine = new AlertEngine();

    public static AlertEngine getInstance(){
        return alertEngine;
    }

    private List alerts = Collections.synchronizedList(new LinkedList());

    private AlertEngine(){}

    public void start(){
        List alertConfigs = ApplicationConfigManager.getAllAlerts();
        for(Iterator it=alertConfigs.iterator(); it.hasNext();){
            AlertConfig alertConfig = (AlertConfig)it.next();
            Alert alert = new Alert(alertConfig);
            alert.register();
        }
        /* get EmailAlerts to start the email delivery thread */
        EmailAlerts.getInstance();

        logger.info("AlertEngine started.");
    }

    public void stop(){
        for(Iterator it=alerts.iterator(); it.hasNext(); ){
            Alert alert = (Alert)it.next();
            alert.unregister();
        }
        // remove all alerts
        alerts.clear();
        logger.info("AlertEngine stopped.");
    }

    public synchronized void updateAlertConfig(AlertConfig alertConfig) {
        Alert alert = new Alert(alertConfig);
        int index = alerts.indexOf(alert);
        if(index != -1){
            // its an existing alert
            Alert oldAlert = (Alert)alerts.remove(index);
            oldAlert.unregister();
        }
        // register the alert
        alert.register();
        // add it to the list
        alerts.add(alert);
    }

    public synchronized void removeAlertConfig(AlertConfig alertConfig){
        Alert alert = new Alert(alertConfig);
        int index = alerts.indexOf(alert);
        if(index != -1){
            Alert oldAlert = (Alert)alerts.remove(index);
            oldAlert.unregister();
        }
    }

    public synchronized void updateApplication(ApplicationConfig config){
        for(Iterator it=config.getAlerts().iterator(); it.hasNext(); ){
            AlertConfig alertConfig = (AlertConfig)it.next();
            updateAlertConfig(alertConfig);
        }
    }

    public synchronized void removeApplication(ApplicationConfig config){
        for(Iterator it=config.getAlerts().iterator(); it.hasNext(); ){
            AlertConfig alertConfig = (AlertConfig)it.next();
            removeAlertConfig(alertConfig);
        }
    }
}
