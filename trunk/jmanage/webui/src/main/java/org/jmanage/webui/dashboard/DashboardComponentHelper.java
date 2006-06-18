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
package org.jmanage.webui.dashboard;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.util.Loggers;
import org.jmanage.webui.dashboard.framework.DashboardComponent;
import org.jmanage.webui.dashboard.framework.DashboardConfig;
import org.jmanage.webui.dashboard.framework.DashboardContext;
import org.jmanage.webui.dashboard.framework.DashboardContextImpl;
import org.jmanage.webui.dashboard.framework.DashboardRepository;
import org.jmanage.webui.util.WebContext;

/**
 *
 * @author Rakesh Kalra
 */
public class DashboardComponentHelper {

    private static final Logger logger = Loggers.getLogger(DashboardComponentHelper.class);
    
    public static String drawComponent(WebContext context, String dashboardId, String componentId){

        DashboardContext dashboardContext = new DashboardContextImpl(context);
        DashboardConfig dashboardConfig = DashboardRepository.getInstance().get(dashboardId);
        assert dashboardConfig != null : "Error retrieving dashboard details. id=" + dashboardId;
        DashboardComponent component = dashboardConfig.getComponents().get(componentId);
        assert component != null : "Error retrieving component. id=" + componentId;
        try{
            return component.draw(dashboardContext);
        }catch(Throwable e){
            logger.log(Level.SEVERE, "Error displaying component", e);
            return "Error:" + e.getMessage();
        }
    }
}
