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

package org.jmanage.webui.actions.config.dashboard;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.dashboard.framework.DashboardConfig;
import org.jmanage.webui.dashboard.framework.DashboardRepository;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.core.config.ApplicationConfig;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO: this should be under the actions.app package. -rk
 * 
 * Date: May 13, 2006 6:35:35 PM
 *
 * @author Shashank Bellary
 */
public class ShowDashboardAction extends BaseAction {

    /**
     * 
     * @param context
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        DashboardRepository instance = DashboardRepository.getInstance();
        ApplicationConfig appConfig = context.getApplicationConfig();
        String currentDashboardId = request.getParameter("dashBID");
        DashboardConfig currentDashboardConfig = null;
        for(String dashboardId : appConfig.getDashboards()){
            if(currentDashboardId.equals(dashboardId)){
                currentDashboardConfig = instance.get(dashboardId);
                break;
            }
        }
        if(currentDashboardConfig == null)
            currentDashboardConfig = instance.get(currentDashboardId);
        request.setAttribute("dashboardPage", currentDashboardConfig.getTemplate());
        
        /*set current page for navigation*/
        request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, currentDashboardConfig.getName());
        
        return mapping.findForward(Forwards.SUCCESS);
    }
}