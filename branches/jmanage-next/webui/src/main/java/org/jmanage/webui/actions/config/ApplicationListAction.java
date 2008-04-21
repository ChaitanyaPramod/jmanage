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
package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.util.WebContext;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.services.AlertService;
import org.jmanage.core.services.ServiceFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * Date: Jun 19, 2004
 * @author  Shashank
 */
public class ApplicationListAction extends BaseAction {

    /**
     * List all configured applications.
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception{
        /* applications */
        List applications = ApplicationConfigManager.getApplications();
        request.setAttribute(RequestAttributes.APPLICATIONS, applications);

        /* alerts */
        AlertService alertService = ServiceFactory.getAlertService();
        List alerts = alertService.getConsoleAlerts(context.getServiceContext());
        request.setAttribute("alerts", alerts);

        return mapping.findForward(Forwards.SUCCESS);
    }
}