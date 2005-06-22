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
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.forms.AlertForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date: Jun 21, 2005 12:16:23 PM
 * @author Bhavana
 */
public class RemoveAlertAction extends BaseAction{

    public ActionForward execute(WebContext context, ActionMapping mapping,
                                 ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response){
        ApplicationConfig appConfig = context.getApplicationConfig();
        if(appConfig.removeAlert(
                request.getParameter(RequestParams.ALERT_ID))!=null){
            ApplicationConfigManager.updateApplication(appConfig);
        }
        return mapping.findForward(Forwards.SUCCESS);
    }
}
