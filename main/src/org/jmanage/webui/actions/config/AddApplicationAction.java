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
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.forms.ApplicationForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigFactory;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.data.ApplicationConfigData;
import org.jmanage.core.services.ConfigurationService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.auth.AccessController;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * date:  Jun 25, 2004
 * @author	Rakesh Kalra
 */
public class AddApplicationAction extends BaseAction {

    /**
     * Add a new application
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
        AccessController.canAccess(context.getUser(), ACL_ADD_APPLICATIONS);
        ApplicationForm appForm = (ApplicationForm)actionForm;
        /* create ApplicationConfigData from this form */
        ApplicationConfigData appConfigData = new ApplicationConfigData();
        CoreUtils.copyProperties(appConfigData, appForm);

        ConfigurationService service = ServiceFactory.getConfigurationService();

        service.addApplication(Utils.getServiceContext(context), appConfigData);

        return mapping.findForward(Forwards.SUCCESS);
    }
}
