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
package org.jmanage.webui.actions.config.dashboard;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.DashboardSelectionForm;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.services.AccessController;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Date: Aug 9, 2006 10:07:12 PM
 *
 * @author Shashank Bellary
 */
public class AddSelectedDashboardsAction extends BaseAction {

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
	AccessController.checkAccess(context.getServiceContext(),ACL_ADD_DASHBOARD);
	List<String> appConfigDashboards = new LinkedList<String>();
        DashboardSelectionForm dbForm = (DashboardSelectionForm)actionForm;
        ApplicationConfig appConfig = context.getApplicationConfig();
        for(String dbID : dbForm.getDashboards()){
            appConfigDashboards.add(dbID);
        }
        appConfig.setDashboards(appConfigDashboards);
        ApplicationConfigManager.updateApplication(appConfig);
        return mapping.findForward(Forwards.SUCCESS);
    }
}